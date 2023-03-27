package br.com.ciceroednilson.repository;

import br.com.ciceroednilson.repository.entity.EnumRole;
import br.com.ciceroednilson.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(EnumRole name);
}
