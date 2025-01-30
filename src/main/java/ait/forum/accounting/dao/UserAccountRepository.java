package ait.forum.accounting.dao;

import ait.forum.accounting.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
}
