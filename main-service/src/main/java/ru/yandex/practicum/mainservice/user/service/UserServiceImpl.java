package ru.yandex.practicum.mainservice.user.service;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.exceptions.EmailAlreadyExistsException;
import ru.yandex.practicum.mainservice.user.dto.CreatedUserDto;
import ru.yandex.practicum.mainservice.user.dto.UserDto;
import ru.yandex.practicum.mainservice.user.mapper.UserMapper;
import ru.yandex.practicum.mainservice.user.model.QUser;
import ru.yandex.practicum.mainservice.user.model.User;
import ru.yandex.practicum.mainservice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    @Transactional
    public UserDto createUser(CreatedUserDto createdUserDto) {
        User user = userMapper.createdUserDtoToUser(createdUserDto);
        try {
            return userMapper.userToUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("Пользователь с таким Email уже существует");
        }
    }

    @Override
    public List<UserDto> getUserList(Integer[] uris, Long from, Long size) {
        QUser qUser = QUser.user;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        BooleanExpression condition = null;
        if (uris != null && uris.length > 0) {
            condition = qUser.id.in(uris);
        }

        JPAQuery<UserDto> query = queryFactory
                .select(Projections.constructor(UserDto.class,
                        qUser.id,
                        qUser.name,
                        qUser.email))
                .from(qUser)
                .where(condition)
                .offset(from).limit(size);

        return query.fetch();
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
