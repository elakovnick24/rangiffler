package rangiffler.service;

import jakarta.annotation.Nonnull;
import rangiffler.data.CurrencyValues;
import rangiffler.data.FriendsEntity;
import rangiffler.data.UserEntity;
import rangiffler.data.repository.UserRepository;
import rangiffler.exception.NotFoundException;
import rangiffler.model.FriendJson;
import rangiffler.model.FriendState;
import rangiffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class UserDataService {

    private static final CurrencyValues DEFAULT_USER_CURRENCY = CurrencyValues.RUB;
    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nonnull
    UserJson update(@Nonnull UserJson user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        if (userEntity == null) {
            throw new NotFoundException("Can`t find user by username: " + user.getUsername());
        }
        userEntity.setFirstname(user.getFirstname());
        userEntity.setSurname(user.getSurname());
        userEntity.setCurrency(user.getCurrency());
        userEntity.setPhoto(user.getPhoto() != null ? user.getPhoto().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }

    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userDataEntity = userRepository.findByUsername(username);
        if (userDataEntity == null) {
            userDataEntity = new UserEntity();
            userDataEntity.setUsername(username);
            userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
            return UserJson.fromEntity(userRepository.save(userDataEntity));
        } else {
            return UserJson.fromEntity(userDataEntity);
        }
    }

    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        Map<UUID, UserJson> result = new HashMap<>();
        for (UserEntity user : userRepository.findByUsernameNot(username)) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
                        .filter(i -> i.getFriend().getUsername().equals(username))
                        .findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
                        .filter(i -> i.getUser().getUsername().equals(username))
                        .findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_RECEIVED
                            : FriendState.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_SENT
                            : FriendState.FRIEND));
                }
            }
            if (!result.containsKey(user.getId())) {
                result.put(user.getId(), UserJson.fromEntity(user));
            }
        }
        return new ArrayList<>(result.values());
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return userEntity
                .getFriends()
                .stream()
                .filter(fe -> includePending || !fe.isPending())
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return userEntity
                .getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendState.INVITE_RECEIVED))
                .toList();
    }

    public UserJson addFriend(@Nonnull String username, @Nonnull FriendJson friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendEntity = userRepository.findByUsername(friend.getUsername());
        if (currentUser == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        if (friendEntity == null) {
            throw new NotFoundException("Can`t find user by username: " + friend.getUsername());
        }
        currentUser.addFriends(true, friendEntity);
        userRepository.save(currentUser);
        return UserJson.fromEntity(friendEntity, FriendState.INVITE_SENT);
    }

    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity inviteUser = userRepository.findByUsername(invitation.getUsername());
        if (currentUser == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        if (inviteUser == null) {
            throw new NotFoundException("Can`t find user by username: " + invitation.getUsername());
        }

        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().getUsername().equals(inviteUser.getUsername()))
                .findFirst()
                .orElseThrow();

        invite.setPending(false);
        currentUser.addFriends(false, inviteUser);
        userRepository.save(currentUser);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }

    @Transactional
    public @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToDecline = userRepository.findByUsername(invitation.getUsername());
        if (currentUser == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        if (friendToDecline == null) {
            throw new NotFoundException("Can`t find user by username: " + invitation.getUsername());
        }

        currentUser.removeInvites(friendToDecline);
        friendToDecline.removeFriends(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToDecline);

        return currentUser.getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendState.INVITE_RECEIVED))
                .toList();
    }

    @Transactional
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username, @Nonnull String friendUsername) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToRemove = userRepository.findByUsername(friendUsername);
        if (currentUser == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        if (friendToRemove == null) {
            throw new NotFoundException("Can`t find user by username: " + friendToRemove);
        }

        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        friendToRemove.removeFriends(currentUser);
        friendToRemove.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToRemove);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }
}
