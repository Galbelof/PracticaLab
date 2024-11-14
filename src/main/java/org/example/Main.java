import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GitUploader {

    public static void main(String[] args) {
        // Rutas y URL del repositorio
        String folderPath = "ruta/a/tu/carpeta"; // Cambia esta ruta a la de tu carpeta
        String repoUrl = "https://github.com/usuario/nombre-del-repo.git"; // Cambia a la URL de tu repositorio
        String commitMessage = "Subiendo archivos desde Java";

        // Ejecutar el proceso de subida a GitHub
        try {
            uploadToGitHub(folderPath, repoUrl, commitMessage);
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }

    public static void uploadToGitHub(String folderPath, String repoUrl, String commitMessage) throws IOException, InterruptedException {
        File directory = new File(folderPath);

        // Inicializar repositorio Git si no está inicializado
        if (!new File(directory, ".git").exists()) {
            runCommand(directory, "git", "init");
            System.out.println("Repositorio Git inicializado.");
        }

        // Añadir origen remoto si no existe
        Process process = runCommand(directory, "git", "remote", "-v");
        if (!processOutputContains(process, "origin")) {
            runCommand(directory, "git", "remote", "add", "origin", repoUrl);
            System.out.println("Origen remoto añadido: " + repoUrl);
        }

        // Agregar todos los archivos, realizar commit y subir
        runCommand(directory, "git", "add", ".");
        runCommand(directory, "git", "commit", "-m", commitMessage);
        System.out.println("Commit realizado con mensaje: '" + commitMessage + "'");

        runCommand(directory, "git", "push", "-u", "origin", "main");
        System.out.println("Archivos subidos correctamente a GitHub.");
    }

    private static Process runCommand(File directory, String... command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(directory);
        Process process = builder.start();
        process.waitFor();
        return process;
    }

    private static boolean processOutputContains(Process process, String text) throws IOException {
        String output = new String(process.getInputStream().readAllBytes());
        return output.contains(text);
    }
}
