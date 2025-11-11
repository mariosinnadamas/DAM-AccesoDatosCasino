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

            Path rutaDestino = Path.of(ruta, "Copia de Seguridad");
            File destino = new File(String.valueOf(rutaDestino));


            if  (!destino.exists()) {
                destino.mkdirs();
            }
            copiar(carpetaPadre, destino);

        } catch (Exception e) {
            System.out.println(e.getMessage());}
    }

    /**
     * Este metodo sincroniza los XML para que tenga los datos del JSON.
     * Para sincronizarlo se borra el contenido del XML y se sobreescribe por el del JSON
     * @throws IOException
     */
    public void sincronizarXML() throws IOException{
        try(FileWriter fw = new FileWriter(xml.fileCliente)){
             xml.fileCliente.delete();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        xml.addListaClientes(json.leerListaClientes());
    }

    /**
     * Sincroniza los JSON para que tenga los datos del XML
     * Para sincronizarlo se borra el contenido del JSON y se sobreescribe por el del XML
     * @throws IOException
     */
    public void sincronizarJSON() throws IOException{
        try(FileWriter fw = new FileWriter(json.fileCliente)){
            fw.write("");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        json.addListaClientes(xml.leerListaClientes());
    }
}
