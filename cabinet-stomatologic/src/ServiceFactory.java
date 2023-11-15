import repository.BinaryFileRepository;
import repository.PacientTextFileRepository;
import repository.ProgramareTextFileRepository;
import servicii.PacientService;
import servicii.PacientServiceImpl;
import servicii.ProgramareService;
import servicii.ProgramareServiceImpl;

public class ServiceFactory {
    public static PacientService createPacientService(String repositoryType, String patientsFileName, BinaryFileRepository pacientRepository) {
        if ("binary".equals(repositoryType)) {
            return new PacientServiceImpl(pacientRepository);
        } else if ("text".equals(repositoryType)) {
            return new PacientServiceImpl(new PacientTextFileRepository(patientsFileName));
        } else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }
    }

    public static ProgramareService createProgramareService(String repositoryType, String appointmentsFileName, BinaryFileRepository programareRepository, PacientTextFileRepository pacientRepository) {
        if ("binary".equals(repositoryType)) {
            return new ProgramareServiceImpl(programareRepository);
        } else if ("text".equals(repositoryType)) {
            return new ProgramareServiceImpl(new ProgramareTextFileRepository(appointmentsFileName, pacientRepository));
        } else {
            throw new IllegalArgumentException("Invalid repository type: " + repositoryType);
        }
    }
}
