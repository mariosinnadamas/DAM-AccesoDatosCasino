package casino;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CasinoDAOFile {
    CasinoDAOFileJSON json = new CasinoDAOFileJSON();
    CasinoDAOFileXML xml = new CasinoDAOFileXML();

    private void copiar(File archivo, File destino) throws IOException {
        if (!archivo.isDirectory()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(destino))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                    bw.flush();
                    destino.setReadOnly();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else {
            destino.mkdir();
            File[] archivos = archivo.listFiles();
            for (File file : archivos) {
                Path nuevaRuta =  Path.of(String.valueOf(destino), file.getName());
                copiar(file, new File(nuevaRuta.toString()));
            }
        }

    }

    public void crearCopiaSeguridad(String ruta) throws IOException {
        try {
            //Path a carpeta recursos
            Path rutaPadre = json.pathCliente.getParent().getParent();
            File carpetaPadre = new File(rutaPadre.toString());

            Path rutaDestino = Path.of(ruta);
            File destino = new File(String.valueOf(rutaDestino));


            if  (!destino.exists()) {
                destino.mkdir();
            }
            copiar(carpetaPadre, destino);

        } catch (Exception e) {
            System.out.println(e.getMessage());}
    }
}
