package account.security.userdetails.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    @PostConstruct
    public void init() {
        var size = repository.count();
        System.out.println("INITIAL SIZE: " + size);
        if (size == 0) {
            repository.saveAll(
                    Arrays.stream(RoleEnum.values())
                            .map(role -> {
                                var r = new Role();
                                r.setRole(role);
                                return r;
                            })
                            .collect(Collectors.toList())
            );
        }
    }
}