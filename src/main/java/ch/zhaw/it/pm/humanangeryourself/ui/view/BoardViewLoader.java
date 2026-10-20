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

public class BoardViewLoader {
    private static final String FXML_NAME = "BoardView.fxml";

    // Attributes whose numeric values should be multiplied
    private static final Set<String> ATTRIBUTES_TO_MULTIPLY = Set.of(
            "prefHeight",
            "prefWidth",
            "height",
            "width",
            "x",
            "y"
    );

    private final URL fxmlResource = Objects.requireNonNull(getClass().getResource(FXML_NAME));

    private BoardViewController controller;
    private Scene scene;

    public BoardViewLoader(int desiredSize) {
        try {
            scaleFxml(desiredSize, fxmlResourceAsPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to scale FXML file: " + fxmlResource, e);
        }

        load();
    }

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

    private static @NotNull Transformer getTransformer() throws TransformerConfigurationException {
        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                OutputKeys.ENCODING, "UTF-8"
        );
        return transformer;
    }

    private Path fxmlResourceAsPath() {
        try {
            return Path.of(Objects.requireNonNull(fxmlResource).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

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

    public BoardViewController getController() {
        return controller;
    }

    public Scene getScene() {
        return scene;
    }
}
