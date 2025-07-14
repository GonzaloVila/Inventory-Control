package com.example.demo.repository;

import com.example.demo.model.ERol;
import com.example.demo.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long>{
    Optional<Rol> findByName(ERol name);
}
