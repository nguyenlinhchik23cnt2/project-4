package org.nguyenlinhchi.dogiadung.SERVICE;



import org.nguyenlinhchi.dogiadung.ENTITY.User;
import org.nguyenlinhchi.dogiadung.REPOSITORY.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public User add(User user) {
        return repository.save(user);
    }

    public User update(Integer id, User user) {
        if (!repository.existsById(id)) {
            return null;
        }
        user.setUserId(id);
        return repository.save(user);
    }

    public String delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Xóa người dùng thành công!";
        }
        return "Người dùng không tồn tại!";
    }
}
