package casino;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class CasinoDAOFile {
    CasinoDAOFileJSON json = new CasinoDAOFileJSON();
    CasinoDAOFileXML xml = new CasinoDAOFileXML();

    public void crearCopiaSeguridad(String ruta, String nombreCarpeta){
        try {
            Path clienteJson = Paths.get(json.fileCliente.toURI());
            Path servicioJson;
            Path logJson;
            Path clienteXml;
            Path servicioXml;
            Path logXml;


            Files.copy(clienteJson, Paths.get(ruta), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
