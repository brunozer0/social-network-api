package br.com.application.quarkussocial.domain.repository;

import br.com.application.quarkussocial.domain.model.Follower;
import br.com.application.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user) {

       var params = Parameters.with("follower", follower)
               .and("user", user).map();


       PanacheQuery<Follower> query = find("follower =:follower and user =:user", params);

        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();
    }

}
