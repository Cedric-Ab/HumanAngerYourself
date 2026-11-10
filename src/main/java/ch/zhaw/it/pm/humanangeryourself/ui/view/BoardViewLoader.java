package ch.zhaw.it.pm.humanangeryourself.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Loads the board view from {@value #FXML_NAME} and scales it to a desired size.
 * <p>
 * The FXML file is designed for a fixed size. Before it is loaded, all size and position
 * attributes in the file are multiplied by a scaling factor so that the root pane matches
 * the requested size. After loading, the controller and the scene are available through
 * {@link #getController()} and {@link #getScene()}.
 */
public class BoardViewLoader {
    /** Name of the FXML file, located next to this class. */
    private static final String FXML_NAME = "BoardView.fxml";

    /** Names of the FXML attributes whose numeric values are multiplied by the scaling factor. */
    private static final Set<String> ATTRIBUTES_TO_MULTIPLY = Set.of(
            "prefHeight",
            "prefWidth",
            "height",
            "width",
            "x",
            "y"
    );

    /** Location of the FXML file, never {@code null}. */
    private final URL fxmlResource = Objects.requireNonNull(getClass().getResource(FXML_NAME));

    /** Controller created by the {@link FXMLLoader}. */
    private BoardViewController controller;

    /** Scene containing the loaded board view. */
    private Scene scene;

    /**
     * Scales the board view FXML file to the desired size and loads it.
     *
     * @param desiredSize desired width of the board view in pixels, which is the
     *                    {@code prefWidth} of the root pane after scaling
     * @throws RuntimeException if the FXML file cannot be scaled or loaded
     */
    public BoardViewLoader(int desiredSize) {
        try {
            scaleFxml(desiredSize, fxmlResourceAsPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to scale FXML file: " + fxmlResource, e);
        }

        load();
    }

    /**
     * Scales all size and position attributes of the FXML file at the given path and writes the
     * result back to the same file. The scaling factor is the desired size divided by the
     * {@code prefWidth} of the root pane. If the factor is 1, the file is left untouched.
     *
     * @param desiredSize desired width of the root pane in pixels
     * @param path        path of the FXML file to scale, which is overwritten
     * @throws RuntimeException if the root element is not a {@code StackPane}
     * @throws Exception        if the file cannot be read, parsed or written
     */
    private static void scaleFxml(int desiredSize, Path path) throws Exception {

        DocumentBuilderFactory factory = getDocumentBuilderFactory();

        Document document;

        try (var input = Files.newInputStream(path)) {
            document = factory.newDocumentBuilder().parse(input);
        }

        NodeList nodes = document.getElementsByTagName("*");

        Element rootPane = (Element) nodes.item(0);
        if (!"StackPane".equals(rootPane.getNodeName()))
            throw new RuntimeException("Root element is not a StackPane: " + rootPane.getNodeName());

        double currentSize = Double.parseDouble(rootPane.getAttribute("prefWidth"));
        double factor = desiredSize / currentSize;
        if (factor == 1) return;

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            scaleAttributes(factor, element.getAttributes());
        }

        Transformer transformer = getTransformer();

        try (OutputStream output = Files.newOutputStream(path)) {
            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(output)
            );
        }
    }

    /**
     * Multiplies all numeric attributes listed in {@link #ATTRIBUTES_TO_MULTIPLY} by the given
     * factor. Attributes with other names or non-numeric values are left unchanged.
     *
     * @param factor     factor to multiply the values with
     * @param attributes attributes of one XML element, modified in place
     */
    private static void scaleAttributes(double factor, NamedNodeMap attributes) {
        for (int j = 0; j < attributes.getLength(); j++) {
            Attr attribute = (Attr) attributes.item(j);

            if (!ATTRIBUTES_TO_MULTIPLY.contains(attribute.getName())) {
                continue;
            }

            String value = attribute.getValue().trim();

            try {
                double number = Double.parseDouble(value);
                attribute.setValue(
                        Double.toString(number * factor)
                );
            } catch (NumberFormatException ignored) {
                // Leave non-numeric values unchanged
            }
        }
    }

    /**
     * Creates an XML parser factory that is hardened against XML external entity (XXE) attacks.
     * Document type declarations, external entities and XInclude are all disabled.
     *
     * @return the configured factory
     * @throws ParserConfigurationException if the parser does not support one of the security features
     */
    private static @NotNull DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Prevent external XML entities
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl", true
        );
        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities", false
        );
        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities", false
        );
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory;
    }

    /**
     * Creates a transformer that writes an XML document as indented UTF-8.
     *
     * @return the configured transformer
     * @throws TransformerConfigurationException if the transformer cannot be created
     */
    private static @NotNull Transformer getTransformer() throws TransformerConfigurationException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                OutputKeys.ENCODING, "UTF-8"
        );
        return transformer;
    }

    /**
     * Converts the URL of the FXML resource to a file system path.
     *
     * @return the path of the FXML file
     * @throws RuntimeException if the URL is not a valid URI
     */
    private Path fxmlResourceAsPath() {
        try {
            return Path.of(Objects.requireNonNull(fxmlResource).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the FXML file and stores the resulting controller and scene.
     *
     * @throws RuntimeException if the FXML file cannot be loaded
     */
    private void load() {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlResource);
            Pane rootNode = loader.load();

            controller = loader.getController();
            scene = new Scene(rootNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the controller of the loaded board view.
     *
     * @return the board view controller
     */
    public BoardViewController getController() {
        return controller;
    }

    /**
     * Returns the scene containing the loaded board view.
     *
     * @return the scene with the scaled board view
     */
    public Scene getScene() {
        return scene;
    }
}
