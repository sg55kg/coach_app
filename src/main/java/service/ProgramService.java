package service;

import model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProgramRepo;

import java.util.List;

@Service
public class ProgramService {

    private ProgramRepo _programRepo;

    @Autowired
    public ProgramService(ProgramRepo programRepo) {
        this._programRepo = programRepo;
    }

    public Program addProgram(Program program) {
        return _programRepo.save(program);
    }

    public List<Program> getPrograms() {
        return _programRepo.findAll();
    }
}
