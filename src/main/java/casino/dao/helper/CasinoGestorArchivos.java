package casino.dao.helper;

import casino.dao.impl.CasinoDAOFileJSON;
import casino.dao.impl.CasinoDAOFileXML;
import exceptions.ClientAlreadyExistsException;
import exceptions.ValidacionException;

import java.io.*;
import java.nio.file.Path;

public class CasinoGestorArchivos {
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
                Path nuevaRuta =  Path.of(String.valueOf(destino), "copia-" + file.getName());
                copiar(file, new File(nuevaRuta.toString()));
            }
        }

    }

    /**
     * Establece los archivos y un único nivel de subcarpetas como ReadOnly o Writeable
     * @param ruta Ruta del directorio a modificar
     * @param bool True para hacerlos Writeable, False para hacerlos SetReadOnly
     * @throws IOException
     */
    public void setDirectoryWriteable(String ruta, boolean bool) {
        File directorio = new File(ruta);
        File[] directories = directorio.listFiles();
        for (File subdirectory : directories) {
            if (subdirectory.isFile()) {
                if (!bool){
                    subdirectory.setReadOnly();
                } else {
                    subdirectory.setWritable(true);
                }
            }
            File[] files = subdirectory.listFiles();
            for (File file: files){
                if (!bool){
                    file.setReadOnly();
                } else {
                    file.setWritable(true);
                }

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


            setDirectoryWriteable(String.valueOf(rutaDestino), true);
            if  (!destino.exists()) {
                destino.mkdirs();
            }
            copiar(carpetaPadre, destino);
            setDirectoryWriteable(String.valueOf(rutaDestino), false);

        } catch (Exception e) {
            System.out.println(e.getMessage());}
    }

    /**
     * Este metodo sincroniza los XML para que tenga los datos del JSON.
     * Para sincronizarlo se borra el contenido del XML y se sobreescribe por el del JSON
     * @throws IOException
     */
    public void sincronizarXML() throws IOException, ValidacionException {
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
    public void sincronizarJSON() throws IOException, ValidacionException, ClientAlreadyExistsException {
        try(FileWriter fw = new FileWriter(json.fileCliente)){
            fw.write("");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        json.addListaClientes(xml.leerListaClientes());
    }
}
