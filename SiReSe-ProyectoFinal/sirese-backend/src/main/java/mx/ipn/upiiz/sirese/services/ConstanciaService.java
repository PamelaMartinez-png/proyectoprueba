package mx.ipn.upiiz.sirese.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import mx.ipn.upiiz.sirese.entities.Aspirante;
import mx.ipn.upiiz.sirese.entities.Constancia;
import mx.ipn.upiiz.sirese.repositories.AspiranteRepository;
import mx.ipn.upiiz.sirese.repositories.ConstanciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ConstanciaService {

    @Autowired private ConstanciaRepository constanciaRepository;
    @Autowired private AspiranteRepository  aspiranteRepository;

    public List<Constancia> listarTodas() {
        return constanciaRepository.findAllByOrderByFechaEmisionDesc();
    }

    /**
     * Genera el PDF de constancia para el aspirante dado y registra la emisión en BD.
     * @return bytes del PDF, o null si el aspirante no existe.
     */
    public byte[] generarYRegistrar(Long aspiranteId) {
        Optional<Aspirante> opt = aspiranteRepository.findById(aspiranteId);
        if (opt.isEmpty()) return null;

        Aspirante a = opt.get();
        byte[] pdf = generarPdf(a);

        // Registrar la emisión en la tabla constancias
        Constancia c = new Constancia();
        c.setAspirante(a);
        constanciaRepository.save(c);

        return pdf;
    }

    // ---- Generación del PDF con OpenPDF ----

    private byte[] generarPdf(Aspirante a) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.LETTER);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font titleFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font boldFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Paragraph header = new Paragraph();
            header.setAlignment(Element.ALIGN_CENTER);
            header.add(new Chunk("Instituto Politécnico Nacional\n", boldFont));
            header.add(new Chunk("Unidad Profesional Interdisciplinaria de Ingeniería campus Zacatecas\n", normalFont));
            header.add(new Chunk("Unidad de Educación Continua\n\n", normalFont));
            doc.add(header);

            doc.add(new Paragraph("CONSTANCIA", titleFont));
            doc.add(new Paragraph("A QUIEN CORRESPONDA:\n\n", boldFont));
            doc.add(new Paragraph("Por medio de la presente se hace constar que:\n\n", normalFont));

            Paragraph nombre = new Paragraph(a.getNombre(), boldFont);
            nombre.setAlignment(Element.ALIGN_CENTER);
            doc.add(nombre);

            doc.add(new Paragraph(
                "\ncon correo electrónico " + a.getEmail()
                + ", se encuentra registrado(a) como aspirante en la carrera de:\n", normalFont));

            Paragraph carrera = new Paragraph(a.getCarrera().getNombre(), boldFont);
            carrera.setAlignment(Element.ALIGN_CENTER);
            doc.add(carrera);

            doc.add(new Paragraph(
                "\nFecha de registro: " + a.getFechaRegistro()
                + "\n\nLo anterior se hace constar para los fines que al interesado convengan.\n\n\n\n",
                normalFont));

            Paragraph firma = new Paragraph(
                "_______________________________\nAdministrador del Sistema\nSiReSe - UPIIZ IPN", normalFont);
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return null;
        }
    }

    public long contarTodas() {
        return constanciaRepository.count();
    }
}
