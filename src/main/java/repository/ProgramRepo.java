package repository;

import model.Program;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Repository
public class ProgramRepo implements IProgramRepo {

    @Override
    public List<Program> findAll() {
        return null;
    }

    @Override
    public List<Program> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Program> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Program> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(Program entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Program> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Program> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Program> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Program> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Program> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Program> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Program> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Program getOne(UUID uuid) {
        return null;
    }

    @Override
    public Program getById(UUID uuid) {
        return null;
    }

    @Override
    public Program getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends Program> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Program> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Program> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Program> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Program> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Program> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Program, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
