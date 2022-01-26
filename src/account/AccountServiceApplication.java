package account;

import account.security.userdetails.repository.RoleEnum;
import account.security.userdetails.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class AccountServiceApplication {

    @Autowired
    RoleRepository rep;

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Component
    class MyRunner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
//            System.out.println("Runner");
//            var role = new Rolee();
//            role.setRole(RoleEnum.ADMINISTRATOR);
//            var savedRole = rep.save(role);
//            System.out.println(savedRole);
//            System.out.println(savedRole.getAuthority());
//            System.out.println(rep.findAll());
        }
    }
}