import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GeneratorPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JFileChooser fc;
	private JFileChooser fc2;
	private JButton openButton;
	private JButton openButton2;
	private JButton generateButton;

	private JTextField filePath;
	private JTextField filePath2;
	private JTextField dofs;
	private JCheckBox noFing;
	private JCheckBox circular;
	private JCheckBox opposable;

	private JLabel thumbsLabel;
	private JTextField thumbs;
	private JSlider slider;
	private JTextField config;
	private JFrame frame;

	boolean check1 = true;
	boolean check2 = true;
	boolean check3 = true;
	boolean check4 = true;

	// important variables

	int dofsVar = 0;
	boolean noFingVar = false;
	boolean circularVar = false;
	boolean opposableVar = false;
	int thumbsVar = 0;
	String configVar = "";
	int fingersnumberVar;
	int fingerdistanceVar;
	String[] fingersconfigurationVar;
	String filePathVar = "";
	String filePath2Var = "";

	public GeneratorPanel() {
		super(new GridLayout(10, 2));

		fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("IV file", "iv"));

		openButton = new JButton("Select body");
		openButton.addActionListener(this);
		add(openButton);

		filePath = new JTextField();
		add(filePath);

		// @@@@
		fc2 = new JFileChooser();
		fc2.setFileFilter(new FileNameExtensionFilter("IV file", "iv"));

		openButton2 = new JButton("Select head");
		openButton2.addActionListener(this);
		add(openButton2);

		filePath2 = new JTextField();
		add(filePath2);
		// @@@@

		add(new JLabel("DOFS"));
		dofs = new JTextField();
		add(dofs);

		noFing = new JCheckBox("No finger Opposition");
		noFing.addActionListener(this);
		add(noFing);
		add(new JLabel(""));

		circular = new JCheckBox("Circular Base");
		circular.addActionListener(this);
		add(circular);
		add(new JLabel(""));

		opposable = new JCheckBox("i-opposable-thumb");
		opposable.addActionListener(this);
		add(opposable);
		add(new JLabel(""));

		thumbsLabel = new JLabel("Num. of Thumbs");
		thumbsLabel.setVisible(false);
		add(thumbsLabel);
		thumbs = new JTextField();
		thumbs.setVisible(false);
		add(thumbs);

		add(new JLabel("Distance Between Fingers"));
		slider = new JSlider();
		slider.setMaximum(2);
		slider.setMinimum(1);
		slider.setMajorTickSpacing(1);
		slider.setValue(1);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		add(slider);

		add(new JLabel("Fingers Configuration: x1,x2,..."));
		config = new JTextField();
		add(config);

		add(new JLabel(""));
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		add(generateButton);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == openButton) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filePath.setText(fc.getSelectedFile().getAbsolutePath());
			}
		} else if (source == openButton2) {
			int returnVal = fc2.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filePath2.setText(fc2.getSelectedFile().getAbsolutePath());
			}
		} else if (source == opposable) {
			if (opposable.isSelected()) {
				circular.setSelected(false);
				noFing.setSelected(false);
				thumbsLabel.setVisible(true);
				thumbs.setVisible(true);
			} else {
				thumbsLabel.setVisible(false);
				thumbs.setVisible(false);
			}
		} else if (source == circular) {
			if (circular.isSelected()) {
				noFing.setSelected(false);
				opposable.setSelected(false);
				thumbsLabel.setVisible(false);
				thumbs.setVisible(false);
			}
		} else if (source == noFing) {
			if (noFing.isSelected()) {
				circular.setSelected(false);
				opposable.setSelected(false);
				thumbsLabel.setVisible(false);
				thumbs.setVisible(false);
			}
		} else if (source == generateButton) {

			if (thumbs.getText().equals(""))
				thumbs.setText("0");

			int dofssum = 0;
			fingersconfigurationVar = config.getText().split("\\,");
			fingersnumberVar = fingersconfigurationVar.length;

			for (int c = 0; c < fingersconfigurationVar.length; c++)
				dofssum += Integer.parseInt(fingersconfigurationVar[c]);

			if (dofs.getText().equals("")) {
				dofs.setText("0");
				check1 = false;
				System.out.println("Please speciify the dofs");
			}

			if (Integer.parseInt(dofs.getText()) != dofssum) {
				check2 = false;
				System.out
						.println("The dofs do not match with the fingers configuration");
			}

			if (!noFing.isSelected() && !circular.isSelected()
					&& !opposable.isSelected()) {
				check3 = false;
				System.out.println("Please select a disposition");
			}
			if (filePath.getText().equals("") || filePath2.getText().equals("")) {
				check4 = false;
				System.out.println("Please select the IV files");
			}

			if (!check1 || !check2 || !check3 || !check4) {
				if (!check1) {
					JOptionPane.showMessageDialog(frame,
							"Please speciify the dofs!", "Error",
							JOptionPane.WARNING_MESSAGE);
					check1 = true;
				}
				if (!check2) {
					JOptionPane
							.showMessageDialog(
									frame,
									"The dofs do not match with the fingers configuration. Please fix it!",
									"Error", JOptionPane.WARNING_MESSAGE);
					check2 = true;
				}
				if (!check3) {
					JOptionPane
							.showMessageDialog(
									frame,
									"No disposition is selecet. Please select a disposition!",
									"Error", JOptionPane.WARNING_MESSAGE);
					check3 = true;
				}
				if (!check4) {
					JOptionPane.showMessageDialog(frame,
							"No IV files are selecet. Please select IV files!",
							"Error", JOptionPane.WARNING_MESSAGE);
					check4 = true;
				}
			} else {
				filePathVar = filePath.getText();
				filePath2Var = filePath2.getText();
				fingerdistanceVar = slider.getValue();
				dofsVar = Integer.parseInt(dofs.getText());
				noFingVar = noFing.isSelected();
				circularVar = circular.isSelected();
				opposableVar = opposable.isSelected();
				thumbsVar = Integer.parseInt(thumbs.getText());

				writeXMLFile(dofsVar, noFingVar, circularVar, opposableVar,
						thumbsVar, fingersnumberVar, fingerdistanceVar,
						fingersconfigurationVar, filePathVar, filePath2Var);
				try {
					Runtime.getRuntime().exec("openrave barretthand.robot.xml");
					// Runtime.getRuntime().exec("openrave.py --database grasping --robot=barretthand.robot.xml --show");
				} catch (Exception p) {
					System.out.println(p.getMessage());
					p.printStackTrace();
				}
			}
		}
	}

	public void writeXMLFile(int dofs, boolean noFing,
			boolean circular, boolean opposable, int thumbs,
			int fingersnumber, int fingerdistance,
			String[] fingersconfiguration, String filePath,
			String filePath2) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Robot");
			doc.appendChild(rootElement);

			// kinbody elements
			Element kinbody = doc.createElement("KinBody");
			rootElement.appendChild(kinbody);

			// set attribute to kinbody element
			Attr file = doc.createAttribute("file");
			file.setValue("barretthand.kinbody.xml");
			kinbody.setAttributeNode(file);

			// kinbody elements
			Element manipulator = doc.createElement("Manipulator");
			rootElement.appendChild(manipulator);

			// set attribute to kinbody element
			Attr name = doc.createAttribute("name");
			name.setValue("hand");
			manipulator.setAttributeNode(name);

			// shorten way
			// kinbody.setAttribute("attribute", "value");

			// base elements
			Element base = doc.createElement("base");
			base.appendChild(doc.createTextNode("palm"));
			manipulator.appendChild(base);

			// effector elements
			Element effector = doc.createElement("effector");
			effector.appendChild(doc.createTextNode("palm"));
			manipulator.appendChild(effector);

			// translation elements
			Element translation = doc.createElement("translation");
			translation.appendChild(doc.createTextNode("-0.047 0 0.08"));
			manipulator.appendChild(translation);

			// joints elements
			Element joints = doc.createElement("joints");
			String joints_string = "";
			for (int i = 0; i < dofs; i++) {
				if (i == 0)
					joints_string = joints_string + "JF" + (i + 1);
				else
					joints_string = joints_string + " " + "JF" + (i + 1);
			}
			joints.appendChild(doc.createTextNode(joints_string));
			manipulator.appendChild(joints);

			// closingdirection elements
			Element closingdirection = doc.createElement("closingdirection");
			String closingdirection_string = "";
			for (int i = 0; i < dofs; i++) {
				if (i == 0)
					closingdirection_string = closingdirection_string + 1;
				else
					closingdirection_string = closingdirection_string + " " + 1;
			}
			closingdirection.appendChild(doc
					.createTextNode(closingdirection_string));
			manipulator.appendChild(closingdirection);

			// direction elements
			Element direction = doc.createElement("direction");
			direction.appendChild(doc.createTextNode("0 0 1"));
			manipulator.appendChild(direction);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					"barretthand.robot.xml"));
			transformer.transform(source, result);

			System.out.println("Done (robot.xml)");
			// ##############################

			// root elements
			Document doc2 = docBuilder.newDocument();
			Element rootElement2 = doc2.createElement("KinBody");
			doc2.appendChild(rootElement2);

			// set attribute to root element
			Attr name2 = doc2.createAttribute("name");
			name2.setValue("BarrettHand");
			rootElement2.setAttributeNode(name2);

			// root elements
			Element body = doc2.createElement("Body");
			rootElement2.appendChild(body);

			// set attribute to root element
			Attr bodyname = doc2.createAttribute("name");
			bodyname.setValue("palm");
			body.setAttributeNode(bodyname);

			// set attribute to root element
			Attr bodytype = doc2.createAttribute("type");
			bodytype.setValue("dynamic");
			body.setAttributeNode(bodytype);

			// define manipulator base
			Element geom = doc2.createElement("Geom");
			body.appendChild(geom);

			// set attribute to body element
			Attr geomtype = doc2.createAttribute("type");
			geomtype.setValue("box");
			geom.setAttributeNode(geomtype);
			if (noFing) {
				// add element to geom
				Element extents = doc2.createElement("Extents");
				// larghezza e lunghezza variano in base al numero di dita e
				// alla
				// disposizione della base
				extents.appendChild(doc2.createTextNode(0.03 * fingersnumber
						* fingerdistance + " 0.01 0.04")); // lunghezza,
				// altezza,
				// larghezza
				geom.appendChild(extents);

				// add element to geom
				Element translation2 = doc2.createElement("Translation");
				translation2.appendChild(doc2.createTextNode(0.03
						* (fingersnumber - 1) * fingerdistance + " -0.046 0"));
				geom.appendChild(translation2);

				// add element to geom
				Element transparency = doc2.createElement("transparency");
				transparency.appendChild(doc2.createTextNode("0.4"));
				geom.appendChild(transparency);

				// define manipulator base
				Element mass = doc2.createElement("mass");
				body.appendChild(mass);

				// set attribute to body element
				Attr masstype = doc2.createAttribute("type");
				masstype.setValue("sphere");
				mass.setAttributeNode(masstype);

				// add element to mass
				Element total = doc2.createElement("total");
				total.appendChild(doc2.createTextNode(Double.toString(0.025
						* fingersnumber * fingerdistance))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(total);

				// add element to mass
				Element radius = doc2.createElement("radius");
				radius.appendChild(doc2.createTextNode(Double
						.toString(0.018 * fingersnumber))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(radius);

				// for each finger it is necessary to have a module on the base
				for (int f = 0; f < fingersnumber; f++) {
					Element geomf = doc2.createElement("Geom");
					body.appendChild(geomf);

					// set attribute to body element
					Attr geomtypef = doc2.createAttribute("type");
					geomtypef.setValue("trimesh");
					geomf.setAttributeNode(geomtypef);

					// add element to geomf
					Element render = doc2.createElement("Render");
					render.appendChild(doc2.createTextNode(filePath
							+ " 0.001")); // to multiply for
					// the number of
					// fingers
					geomf.appendChild(render);

					// add element to geomf
					Element data = doc2.createElement("data");
					data.appendChild(doc2
							.createTextNode(filePath + " 0.001")); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(data);

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb.appendChild(doc2.createTextNode(0.06 * f
							* fingerdistance + " -0.018 -0.007")); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(translationb);
				}
				// we build the fingers
				for (int f = 0; f < fingersnumber; f++) {
					// if the f-th fingers has more than one dof
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]) - 1; m++) {
							// root elements
							Element fingerkinbody = doc2
									.createElement("Kinbody");
							rootElement2.appendChild(fingerkinbody);

							// set attribute to root element
							Attr prefix = doc2.createAttribute("prefix");
							prefix.setValue(Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(prefix);

							// set attribute to root element
							Attr fingerkinbodyname = doc2
									.createAttribute("name");
							fingerkinbodyname.setValue("kPY"
									+ Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(fingerkinbodyname);
							// if (Integer.parseInt(fingersconfiguration[f]) >
							// m)

							// root elements
							Element bodym = doc2.createElement("Body");
							fingerkinbody.appendChild(bodym);

							// set attribute to root element
							Attr bodymname = doc2.createAttribute("name");
							bodymname.setValue("Seg");
							bodym.setAttributeNode(bodymname);

							// set attribute to root element
							Attr bodymtype = doc2.createAttribute("type");
							bodymtype.setValue("dynamic");
							bodym.setAttributeNode(bodymtype);

							// define manipulator base
							Element massm = doc2.createElement("mass");
							bodym.appendChild(massm);

							// set attribute to body element
							Attr massmtype = doc2.createAttribute("type");
							massmtype.setValue("sphere");
							massm.setAttributeNode(massmtype);

							// add element to mass
							Element totalm = doc2.createElement("total");
							totalm.appendChild(doc2.createTextNode("0.05"));
							massm.appendChild(totalm);

							// add element to mass
							Element radiusm = doc2.createElement("radius");
							radiusm.appendChild(doc2.createTextNode("0.036"));
							massm.appendChild(radiusm);

							Element geomm = doc2.createElement("Geom");
							bodym.appendChild(geomm);

							// set attribute to body element
							Attr geomtypem = doc2.createAttribute("type");
							geomtypem.setValue("trimesh");
							geomm.setAttributeNode(geomtypem);

							// add element to geomf
							Element renderm = doc2.createElement("Render");
							renderm.appendChild(doc2
									.createTextNode(filePath2 + " 0.001"));
							geomm.appendChild(renderm);

							// add element to geomf
							Element datam = doc2.createElement("data");
							datam.appendChild(doc2.createTextNode(filePath2
									+ " 0.001"));
							geomm.appendChild(datam);

							// add element to geomf
							Element translationm = doc2
									.createElement("translation");
							translationm.appendChild(doc2
									.createTextNode("0 0.018 -0.007"));
							geomm.appendChild(translationm);

							// Y1-body
							Element geommb = doc2.createElement("Geom");
							bodym.appendChild(geommb);

							// set attribute to body element
							Attr geomtypemb = doc2.createAttribute("type");
							geomtypemb.setValue("trimesh");
							geommb.setAttributeNode(geomtypemb);

							// add element to geomf
							Element rendermb = doc2.createElement("Render");
							rendermb.appendChild(doc2
									.createTextNode(filePath + " 0.001"));
							geommb.appendChild(rendermb);

							// add element to geomf
							Element datamb = doc2.createElement("data");
							datamb.appendChild(doc2.createTextNode(filePath
									+ " 0.001"));
							geommb.appendChild(datamb);

							// add element to geomf
							Element translationmb = doc2
									.createElement("translation");
							translationmb.appendChild(doc2
									.createTextNode("0 0.054 -0.007"));
							geommb.appendChild(translationmb);

							// add element to geomf
							Element offsetfrom = doc2
									.createElement("offsetfrom");
							if (m == 0) {
								offsetfrom.appendChild(doc2
										.createTextNode("palm"));
								fingerkinbody.appendChild(offsetfrom);

								// add element to geomf
								Element translationo = doc2
										.createElement("translation");
								translationo.appendChild(doc2
										.createTextNode(0.06 * f
												* fingerdistance + " " + 0.072
												* m + " " + " 0"));
								fingerkinbody.appendChild(translationo);

							} else {
								offsetfrom.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ m));
								fingerkinbody.appendChild(offsetfrom);

								// add element to geomf
								Element translationo = doc2
										.createElement("translation");
								translationo.appendChild(doc2
										.createTextNode(0.06 * f
												* fingerdistance + " " + 0.072
												* m + " " + " 0"));
								fingerkinbody.appendChild(translationo);

							}
						}
					}
					// otherwise we have to add the head

					// root elements
					Element headkinbody = doc2.createElement("Kinbody");
					rootElement2.appendChild(headkinbody);

					// set attribute to root element
					Attr prefixh = doc2.createAttribute("prefix");
					prefixh.setValue("h" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(prefixh);

					// set attribute to root element
					Attr headkinbodyname = doc2.createAttribute("name");
					headkinbodyname.setValue("khead" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(headkinbodyname);

					// root elements
					Element headbody = doc2.createElement("Body");
					headkinbody.appendChild(headbody);

					// set attribute to root element
					Attr headbodyname = doc2.createAttribute("name");
					headbodyname.setValue("Head");
					headbody.setAttributeNode(headbodyname);

					// set attribute to root element
					Attr headbodytype = doc2.createAttribute("type");
					headbodytype.setValue("dynamic");
					headbody.setAttributeNode(headbodytype);

					// define manipulator base
					Element headmass = doc2.createElement("mass");
					headbody.appendChild(headmass);

					// set attribute to body element
					Attr headmasstype = doc2.createAttribute("type");
					headmasstype.setValue("sphere");
					headmass.setAttributeNode(headmasstype);

					// add element to mass
					Element headtotal = doc2.createElement("total");
					headtotal.appendChild(doc2.createTextNode("0.025"));
					headmass.appendChild(headtotal);

					// add element to mass
					Element headradius = doc2.createElement("radius");
					headradius.appendChild(doc2.createTextNode("0.018"));
					headmass.appendChild(headradius);

					Element headgeom = doc2.createElement("Geom");
					headbody.appendChild(headgeom);

					// set attribute to body element
					Attr headgeomtype = doc2.createAttribute("type");
					headgeomtype.setValue("trimesh");
					headgeom.setAttributeNode(headgeomtype);

					// add element to geomf
					Element headrender = doc2.createElement("Render");
					headrender.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headrender);

					// add element to geomf
					Element headdata = doc2.createElement("data");
					headdata.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headdata);

					// add element to geomf
					Element headtranslation = doc2.createElement("translation");
					headtranslation.appendChild(doc2
							.createTextNode("0 0.018 -0.007"));
					headgeom.appendChild(headtranslation);

					Element headtranslation2 = doc2
							.createElement("translation");
					headtranslation2.appendChild(doc2.createTextNode(0.06 * f
							* fingerdistance + " " + 0.072
							* (Integer.parseInt(fingersconfiguration[f]) - 1)
							+ " " + "0"));
					headkinbody.appendChild(headtranslation2);

				}
				// joints

				int j = 1;
				for (int f = 0; f < fingersnumber; f++) {
					// if the f-th fingers has more than one dof

					// Element Joint = doc2.createElement("Joint");
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]); m++) {
							// -1 apposta per head
							// root elements
							Element Joint = doc2.createElement("Joint");
							rootElement2.appendChild(Joint);

							// set attribute to root element
							Attr jointname = doc2.createAttribute("name");
							jointname.setValue("JF" + Integer.toString(j));
							Joint.setAttributeNode(jointname);
							j++;

							// set attribute to root element
							Attr type = doc2.createAttribute("type");
							type.setValue("hinge");
							Joint.setAttributeNode(type);
							// if it is the first joint
							if (m == 0) {
								Element jointbodypalm = doc2
										.createElement("Body");
								jointbodypalm.appendChild(doc2
										.createTextNode("palm"));
								Joint.appendChild(jointbodypalm);

								Element jointbodypalm2 = doc2
										.createElement("Body");
								jointbodypalm2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodypalm2);

								Element offsetfrompalm = doc2
										.createElement("offsetfrom");
								offsetfrompalm.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrompalm);

								Element weightpalm = doc2
										.createElement("weight");
								weightpalm.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightpalm);

								Element limitsdegpalm = doc2
										.createElement("limitsdeg");
								limitsdegpalm.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegpalm);

								Element axispalm = doc2.createElement("axis");
								axispalm.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axispalm);

								Element maxtorquepalm = doc2
										.createElement("maxtorque");
								maxtorquepalm.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquepalm);

								Element maxvelpalm = doc2
										.createElement("maxvel");
								maxvelpalm.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelpalm);

								Element resolutionpalm = doc2
										.createElement("resolution");
								resolutionpalm.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionpalm);

							} else if (m < Integer
									.parseInt(fingersconfiguration[f]) - 1) {// add
								// the
								// others
								// joints
								// in
								// the
								// middle
								Element jointbodymiddle = doc2
										.createElement("Body");
								jointbodymiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodymiddle);

								Element jointbodymiddle2 = doc2
										.createElement("Body");
								jointbodymiddle2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodymiddle2);

								Element offsetfrommiddle = doc2
										.createElement("offsetfrom");
								offsetfrommiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrommiddle);

								Element weightmiddle = doc2
										.createElement("weight");
								weightmiddle.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightmiddle);

								Element limitsdegmiddle = doc2
										.createElement("limitsdeg");
								limitsdegmiddle.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegmiddle);

								Element axismiddle = doc2.createElement("axis");
								axismiddle.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axismiddle);

								Element maxtorquemiddle = doc2
										.createElement("maxtorque");
								maxtorquemiddle.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquemiddle);

								Element maxvelmiddle = doc2
										.createElement("maxvel");
								maxvelmiddle.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelmiddle);

								Element resolutionmiddle = doc2
										.createElement("resolution");
								resolutionmiddle.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionmiddle);
							}
							// joint head
							if (m == Integer.parseInt(fingersconfiguration[f]) - 1) {
								// attacca head
								Element jointbodyhead = doc2
										.createElement("Body");
								jointbodyhead.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodyhead);

								Element jointbodyhead2 = doc2
										.createElement("Body");
								jointbodyhead2.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(jointbodyhead2);

								Element offsetfromhead = doc2
										.createElement("offsetfrom");
								offsetfromhead.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(offsetfromhead);

								Element weighthead = doc2
										.createElement("weight");
								weighthead.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weighthead);

								Element limitsdeghead = doc2
										.createElement("limitsdeg");
								limitsdeghead.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdeghead);

								Element axishead = doc2.createElement("axis");
								axishead.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axishead);

								Element maxtorquehead = doc2
										.createElement("maxtorque");
								maxtorquehead.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquehead);

								Element maxvelhead = doc2
										.createElement("maxvel");
								maxvelhead.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelhead);

								Element resolutionhead = doc2
										.createElement("resolution");
								resolutionhead.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionhead);
							}

						}

					} else {
						//System.out.println("qui occorre costruire il giunto in caso che ci sia solo un modulo");
						Element Joint = doc2.createElement("Joint");
						rootElement2.appendChild(Joint);

						// set attribute to root element
						Attr jointname = doc2.createAttribute("name");
						jointname.setValue("JF" + Integer.toString(j));
						Joint.setAttributeNode(jointname);
						j++;

						// set attribute to root element
						Attr type = doc2.createAttribute("type");
						type.setValue("hinge");
						Joint.setAttributeNode(type);
						// if it is the first joint

						Element jointbodyheads = doc2.createElement("Body");
						jointbodyheads.appendChild(doc2.createTextNode("palm"));
						Joint.appendChild(jointbodyheads);

						Element jointbodyhead2s = doc2.createElement("Body");
						jointbodyhead2s.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(jointbodyhead2s);

						Element offsetfromheads = doc2
								.createElement("offsetfrom");
						offsetfromheads.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(offsetfromheads);

						Element weightheads = doc2.createElement("weight");
						weightheads.appendChild(doc2.createTextNode("0.14894"));
						Joint.appendChild(weightheads);

						Element limitsdegheads = doc2
								.createElement("limitsdeg");
						limitsdegheads.appendChild(doc2
								.createTextNode("-90 90"));
						Joint.appendChild(limitsdegheads);

						Element axisheads = doc2.createElement("axis");
						axisheads.appendChild(doc2.createTextNode("1 0 0"));
						Joint.appendChild(axisheads);

						Element maxtorqueheads = doc2
								.createElement("maxtorque");
						maxtorqueheads.appendChild(doc2.createTextNode("0.4"));
						Joint.appendChild(maxtorqueheads);

						Element maxvelheads = doc2.createElement("maxvel");
						maxvelheads.appendChild(doc2.createTextNode("4.5"));
						Joint.appendChild(maxvelheads);

						Element resolutionheads = doc2
								.createElement("resolution");
						resolutionheads.appendChild(doc2.createTextNode("1.7"));
						Joint.appendChild(resolutionheads);
					}

				}

			} else if (circular) {
				// add element to geom
				Element extents = doc2.createElement("Extents");
				// larghezza e lunghezza variano in base al numero di dita e
				// alla
				// disposizione della base
				extents.appendChild(doc2.createTextNode(Math.log(1 + 1.3 * 0.03
						* fingersnumber * fingerdistance)
						+ " 0.01 "
						+ Math.log(1 + 1.5 * 0.03 * fingersnumber
								* fingerdistance))); // lunghezza,
				// altezza,
				// larghezza
				geom.appendChild(extents);

				// add element to geom
				Element translation2 = doc2.createElement("Translation");
				translation2.appendChild(doc2.createTextNode("0 -0.046 0"));
				geom.appendChild(translation2);

				// add element to geom
				Element transparency = doc2.createElement("transparency");
				transparency.appendChild(doc2.createTextNode("0.4"));
				geom.appendChild(transparency);

				// define manipulator base
				Element mass = doc2.createElement("mass");
				body.appendChild(mass);

				// set attribute to body element
				Attr masstype = doc2.createAttribute("type");
				masstype.setValue("sphere");
				mass.setAttributeNode(masstype);

				// add element to mass
				Element total = doc2.createElement("total");
				total.appendChild(doc2.createTextNode(Double.toString(0.025
						* fingersnumber * fingerdistance))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(total);

				// add element to mass
				Element radius = doc2.createElement("radius");
				radius.appendChild(doc2.createTextNode(Double
						.toString(0.018 * fingersnumber))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(radius);

				// for each finger it is necessary to have a module on the base
				// boolean check = true;
				for (int f = 0; f < fingersnumber; f++) {
					Element geomf = doc2.createElement("Geom");
					body.appendChild(geomf);

					// set attribute to body element
					Attr geomtypef = doc2.createAttribute("type");
					geomtypef.setValue("trimesh");
					geomf.setAttributeNode(geomtypef);

					// add element to geomf
					Element render = doc2.createElement("Render");
					render.appendChild(doc2.createTextNode(filePath
							+ " 0.001")); // to multiply for
					// the number of
					// fingers
					geomf.appendChild(render);

					// add element to geomf
					Element data = doc2.createElement("data");
					data.appendChild(doc2
							.createTextNode(filePath + " 0.001")); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(data);
					// la prima volta che siamo oltre la metà delle dita
					// ruotiamo il modulo
					if ((f == (int) (fingersnumber / 2)) && f != 0) {
						Element RotationMat = doc2.createElement("RotationMat");
						RotationMat.appendChild(doc2
								.createTextNode("-1 0 0 0 1 0 0 0 -1"));
						geomf.appendChild(RotationMat);
					} else {

						if (Math.sin(f * 2 * Math.PI / fingersnumber) > 0) {
							Element RotationMat = doc2
									.createElement("RotationMat");
							RotationMat.appendChild(doc2
									.createTextNode("0 0 1 0 1 0 -1 0 0")); // to
							// multiply
							// for
							// the number of
							// fingers
							geomf.appendChild(RotationMat);
						}

						if (Math.sin(f * 2 * Math.PI / fingersnumber) < 0) {
							Element RotationMat = doc2
									.createElement("RotationMat");
							RotationMat.appendChild(doc2
									.createTextNode("0 0 -1 0 1 0 1 0 0")); // to
							// multiply
							// for
							// the number of
							// fingers
							geomf.appendChild(RotationMat);
						}
					}

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb.appendChild(doc2.createTextNode(Math.log(1
							+ 0.6 * 0.03 * fingersnumber * fingerdistance)
							* Math.sin(f * 2 * Math.PI / fingersnumber)
							+ " -0.018 "
							+ Math.log(1 + 0.6 * 0.03 * fingersnumber
									* fingerdistance)
							* Math.cos(f * 2 * Math.PI / fingersnumber))); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(translationb);
				}
				// here we create the fingers
				// for each finger
				double offseta = 0;
				double offsetb = 0;
				for (int f = 0; f < fingersnumber; f++) {
					// if the f-th finger has more than one module
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						// we add modules in the middle
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]) - 1; m++) {
							// root elements
							Element fingerkinbody = doc2
									.createElement("Kinbody");
							rootElement2.appendChild(fingerkinbody);

							// set attribute to root element
							Attr prefix = doc2.createAttribute("prefix");
							prefix.setValue(Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(prefix);

							// set attribute to root element
							Attr fingerkinbodyname = doc2
									.createAttribute("name");
							fingerkinbodyname.setValue("kPY"
									+ Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(fingerkinbodyname);
							// if (Integer.parseInt(fingersconfiguration[f]) >
							// m)

							// root elements
							Element bodym = doc2.createElement("Body");
							fingerkinbody.appendChild(bodym);

							// set attribute to root element
							Attr bodymname = doc2.createAttribute("name");
							bodymname.setValue("Seg");
							bodym.setAttributeNode(bodymname);

							// set attribute to root element
							Attr bodymtype = doc2.createAttribute("type");
							bodymtype.setValue("dynamic");
							bodym.setAttributeNode(bodymtype);

							// define manipulator base
							Element massm = doc2.createElement("mass");
							bodym.appendChild(massm);

							// set attribute to body element
							Attr massmtype = doc2.createAttribute("type");
							massmtype.setValue("sphere");
							massm.setAttributeNode(massmtype);

							// add element to mass
							Element totalm = doc2.createElement("total");
							totalm.appendChild(doc2.createTextNode("0.05"));
							massm.appendChild(totalm);

							// add element to mass
							Element radiusm = doc2.createElement("radius");
							radiusm.appendChild(doc2.createTextNode("0.036"));
							massm.appendChild(radiusm);

							Element geomm = doc2.createElement("Geom");
							bodym.appendChild(geomm);

							// set attribute to body element
							Attr geomtypem = doc2.createAttribute("type");
							geomtypem.setValue("trimesh");
							geomm.setAttributeNode(geomtypem);

							// add element to geomf
							Element renderm = doc2.createElement("Render");
							renderm.appendChild(doc2
									.createTextNode(filePath2 + " 0.001"));
							geomm.appendChild(renderm);

							// add element to geomf
							Element datam = doc2.createElement("data");
							datam.appendChild(doc2.createTextNode(filePath2
									+ " 0.001"));
							geomm.appendChild(datam);

							// add element to geomf
							Element translationm = doc2
									.createElement("translation");
							translationm.appendChild(doc2
									.createTextNode("0 0.018 -0.007"));
							geomm.appendChild(translationm);

							// Y1-body
							Element geommb = doc2.createElement("Geom");
							bodym.appendChild(geommb);

							// set attribute to body element
							Attr geomtypemb = doc2.createAttribute("type");
							geomtypemb.setValue("trimesh");
							geommb.setAttributeNode(geomtypemb);

							// add element to geomf
							Element rendermb = doc2.createElement("Render");
							rendermb.appendChild(doc2
									.createTextNode(filePath + " 0.001"));
							geommb.appendChild(rendermb);

							// add element to geomf
							Element datamb = doc2.createElement("data");
							datamb.appendChild(doc2.createTextNode(filePath
									+ " 0.001"));
							geommb.appendChild(datamb);

							// add element to geomf
							Element translationmb = doc2
									.createElement("translation");
							translationmb.appendChild(doc2
									.createTextNode("0 0.054 -0.007"));
							geommb.appendChild(translationmb);

							// add element to geomf
							Element offsetfrom = doc2
									.createElement("offsetfrom");

							offsetfrom.appendChild(doc2.createTextNode(Integer
									.toString(f + 1)
									+ m));
							fingerkinbody.appendChild(offsetfrom);

							if ((f == (int) (fingersnumber / 2)) && f != 0) {
								Element RotationMat = doc2
										.createElement("RotationMat");
								RotationMat.appendChild(doc2
										.createTextNode("-1 0 0 0 1 0 0 0 -1"));
								fingerkinbody.appendChild(RotationMat);
								// check = false;

								offseta = -0.007;
								offsetb = 0;
								//System.out.println("sono quello a meta':" + f);
							} else {

								if (Math.sin(f * 2 * Math.PI / fingersnumber) > 0) {
									Element RotationMat = doc2
											.createElement("RotationMat");
									RotationMat
											.appendChild(doc2
													.createTextNode("0 0 1 0 1 0 -1 0 0")); // to
									// multiply
									// for
									// the number of
									// fingers
									fingerkinbody.appendChild(RotationMat);
									offseta = 0;
									offsetb = 0.007;
									//System.out.println("sono uno di destra:"											+ f);
								}

								if (Math.sin(f * 2 * Math.PI / fingersnumber) < 0) {
									Element RotationMat = doc2
											.createElement("RotationMat");
									RotationMat
											.appendChild(doc2
													.createTextNode("0 0 -1 0 1 0 1 0 0")); // to
									// multiply
									// for
									// the number of
									// fingers
									fingerkinbody.appendChild(RotationMat);
									offseta = 0;
									offsetb = -0.007;
									//System.out.println("sono uno di sinistra:"											+ f);
								}
							}
							if (f == 0) {
								offseta = 0.007;
								offsetb = 0;
								//System.out.println("sono il primo:" + f);
							}

							// add element to geomf
							Element translationb = doc2
									.createElement("translation");
							translationb
									.appendChild(doc2
											.createTextNode((Math.log(1 + 0.6
													* 0.03 * fingersnumber
													* fingerdistance)
													* Math.sin(f * 2 * Math.PI
															/ fingersnumber) + offsetb)
													+ " "
													+ 0.072
													* m
													+ " "
													+ (Math.log(1 + 0.6 * 0.03
															* fingersnumber
															* fingerdistance)
															* Math
																	.cos(f
																			* 2
																			* Math.PI
																			/ fingersnumber) + offseta)));
							fingerkinbody.appendChild(translationb);

						}
					}
					offseta = 0;
					offsetb = 0;
					// otherwise we add the head
					// root elements
					Element headkinbody = doc2.createElement("Kinbody");
					rootElement2.appendChild(headkinbody);

					// set attribute to root element
					Attr prefixh = doc2.createAttribute("prefix");
					prefixh.setValue("h" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(prefixh);

					// set attribute to root element
					Attr headkinbodyname = doc2.createAttribute("name");
					headkinbodyname.setValue("khead" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(headkinbodyname);

					// root elements
					Element headbody = doc2.createElement("Body");
					headkinbody.appendChild(headbody);

					// set attribute to root element
					Attr headbodyname = doc2.createAttribute("name");
					headbodyname.setValue("Head");
					headbody.setAttributeNode(headbodyname);

					// set attribute to root element
					Attr headbodytype = doc2.createAttribute("type");
					headbodytype.setValue("dynamic");
					headbody.setAttributeNode(headbodytype);

					// define manipulator base
					Element headmass = doc2.createElement("mass");
					headbody.appendChild(headmass);

					// set attribute to body element
					Attr headmasstype = doc2.createAttribute("type");
					headmasstype.setValue("sphere");
					headmass.setAttributeNode(headmasstype);

					// add element to mass
					Element headtotal = doc2.createElement("total");
					headtotal.appendChild(doc2.createTextNode("0.025"));
					headmass.appendChild(headtotal);

					// add element to mass
					Element headradius = doc2.createElement("radius");
					headradius.appendChild(doc2.createTextNode("0.018"));
					headmass.appendChild(headradius);

					Element headgeom = doc2.createElement("Geom");
					headbody.appendChild(headgeom);

					// set attribute to body element
					Attr headgeomtype = doc2.createAttribute("type");
					headgeomtype.setValue("trimesh");
					headgeom.setAttributeNode(headgeomtype);

					// add element to geomf
					Element headrender = doc2.createElement("Render");
					headrender.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headrender);

					// add element to geomf
					Element headdata = doc2.createElement("data");
					headdata.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headdata);

					// add element to geomf
					Element headtranslation = doc2.createElement("translation");
					headtranslation.appendChild(doc2
							.createTextNode("0 0.018 -0.007"));
					headgeom.appendChild(headtranslation);

					// add element to geomf
					Element offsetfrom = doc2.createElement("offsetfrom");
					offsetfrom.appendChild(doc2.createTextNode(Integer
							.toString(f + 1)
							+ (Integer.parseInt(fingersconfiguration[f]) - 1)));
					headkinbody.appendChild(offsetfrom);

					if ((f == (int) (fingersnumber / 2)) && f != 0) {
						Element RotationMat = doc2.createElement("RotationMat");
						RotationMat.appendChild(doc2
								.createTextNode("-1 0 0 0 1 0 0 0 -1"));
						headkinbody.appendChild(RotationMat);
						// check = false;

						offseta = -0.007;
						offsetb = 0;
					} else {

						if (Math.sin(f * 2 * Math.PI / fingersnumber) > 0) {
							Element RotationMat = doc2
									.createElement("RotationMat");
							RotationMat.appendChild(doc2
									.createTextNode("0 0 1 0 1 0 -1 0 0")); // to
							// multiply
							// for
							// the number of
							// fingers
							headkinbody.appendChild(RotationMat);
							offseta = 0;
							offsetb = 0.007;
						}

						if (Math.sin(f * 2 * Math.PI / fingersnumber) < 0) {
							Element RotationMat = doc2
									.createElement("RotationMat");
							RotationMat.appendChild(doc2
									.createTextNode("0 0 -1 0 1 0 1 0 0")); // to
							// multiply
							// for
							// the number of
							// fingers
							headkinbody.appendChild(RotationMat);
							offseta = 0;
							offsetb = -0.007;
						}
					}
					if (f == 0) {
						offseta = 0.007;
						offsetb = 0;
					}

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb
							.appendChild(doc2
									.createTextNode((Math.log(1 + 0.6 * 0.03
											* fingersnumber * fingerdistance)
											* Math.sin(f * 2 * Math.PI
													/ fingersnumber) + offsetb)
											+ " "
											+ 0.072
											* (Integer
													.parseInt(fingersconfiguration[f]) - 1)
											+ " "
											+ (Math.log(1 + 0.6 * 0.03
													* fingersnumber
													* fingerdistance)
													* Math.cos(f * 2 * Math.PI
															/ fingersnumber) + offseta)));
					headkinbody.appendChild(translationb);

				}
				// joints
				int j = 1;
				for (int f = 0; f < fingersnumber; f++) {
					// if the f-th fingers has more than one dof
					// Element Joint = doc2.createElement("Joint");
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]); m++) {
							// -1 apposta per head
							// root elements
							Element Joint = doc2.createElement("Joint");
							rootElement2.appendChild(Joint);

							// set attribute to root element
							Attr jointname = doc2.createAttribute("name");
							jointname.setValue("JF" + Integer.toString(j));
							Joint.setAttributeNode(jointname);
							j++;

							// set attribute to root element
							Attr type = doc2.createAttribute("type");
							type.setValue("hinge");
							Joint.setAttributeNode(type);
							// if it is the first joint
							if (m == 0) {
								Element jointbodypalm = doc2
										.createElement("Body");
								jointbodypalm.appendChild(doc2
										.createTextNode("palm"));
								Joint.appendChild(jointbodypalm);

								Element jointbodypalm2 = doc2
										.createElement("Body");
								jointbodypalm2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodypalm2);

								Element offsetfrompalm = doc2
										.createElement("offsetfrom");
								offsetfrompalm.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrompalm);

								Element weightpalm = doc2
										.createElement("weight");
								weightpalm.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightpalm);

								Element limitsdegpalm = doc2
										.createElement("limitsdeg");
								limitsdegpalm.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegpalm);

								Element axispalm = doc2.createElement("axis");
								axispalm.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axispalm);

								Element maxtorquepalm = doc2
										.createElement("maxtorque");
								maxtorquepalm.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquepalm);

								Element maxvelpalm = doc2
										.createElement("maxvel");
								maxvelpalm.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelpalm);

								Element resolutionpalm = doc2
										.createElement("resolution");
								resolutionpalm.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionpalm);

							} else if (m < Integer
									.parseInt(fingersconfiguration[f]) - 1) {// add
								// the
								// others
								// joints
								// in
								// the
								// middle
								Element jointbodymiddle = doc2
										.createElement("Body");
								jointbodymiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodymiddle);

								Element jointbodymiddle2 = doc2
										.createElement("Body");
								jointbodymiddle2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodymiddle2);

								Element offsetfrommiddle = doc2
										.createElement("offsetfrom");
								offsetfrommiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrommiddle);

								Element weightmiddle = doc2
										.createElement("weight");
								weightmiddle.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightmiddle);

								Element limitsdegmiddle = doc2
										.createElement("limitsdeg");
								limitsdegmiddle.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegmiddle);

								Element axismiddle = doc2.createElement("axis");
								axismiddle.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axismiddle);

								Element maxtorquemiddle = doc2
										.createElement("maxtorque");
								maxtorquemiddle.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquemiddle);

								Element maxvelmiddle = doc2
										.createElement("maxvel");
								maxvelmiddle.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelmiddle);

								Element resolutionmiddle = doc2
										.createElement("resolution");
								resolutionmiddle.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionmiddle);
							}
							// joint head
							if (m == Integer.parseInt(fingersconfiguration[f]) - 1) {
								// attacca head
								Element jointbodyhead = doc2
										.createElement("Body");
								jointbodyhead.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodyhead);

								Element jointbodyhead2 = doc2
										.createElement("Body");
								jointbodyhead2.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(jointbodyhead2);

								Element offsetfromhead = doc2
										.createElement("offsetfrom");
								offsetfromhead.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(offsetfromhead);

								Element weighthead = doc2
										.createElement("weight");
								weighthead.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weighthead);

								Element limitsdeghead = doc2
										.createElement("limitsdeg");
								limitsdeghead.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdeghead);

								Element axishead = doc2.createElement("axis");
								axishead.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axishead);

								Element maxtorquehead = doc2
										.createElement("maxtorque");
								maxtorquehead.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquehead);

								Element maxvelhead = doc2
										.createElement("maxvel");
								maxvelhead.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelhead);

								Element resolutionhead = doc2
										.createElement("resolution");
								resolutionhead.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionhead);
							}

						}

					} else {
						//System.out.println("qui occorre costruire il giunto in caso che ci sia solo un modulo");
						Element Joint = doc2.createElement("Joint");
						rootElement2.appendChild(Joint);

						// set attribute to root element
						Attr jointname = doc2.createAttribute("name");
						jointname.setValue("JF" + Integer.toString(j));
						Joint.setAttributeNode(jointname);
						j++;

						// set attribute to root element
						Attr type = doc2.createAttribute("type");
						type.setValue("hinge");
						Joint.setAttributeNode(type);
						// if it is the first joint

						Element jointbodyheads = doc2.createElement("Body");
						jointbodyheads.appendChild(doc2.createTextNode("palm"));
						Joint.appendChild(jointbodyheads);

						Element jointbodyhead2s = doc2.createElement("Body");
						jointbodyhead2s.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(jointbodyhead2s);

						Element offsetfromheads = doc2
								.createElement("offsetfrom");
						offsetfromheads.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(offsetfromheads);

						Element weightheads = doc2.createElement("weight");
						weightheads.appendChild(doc2.createTextNode("0.14894"));
						Joint.appendChild(weightheads);

						Element limitsdegheads = doc2
								.createElement("limitsdeg");
						limitsdegheads.appendChild(doc2
								.createTextNode("-90 90"));
						Joint.appendChild(limitsdegheads);

						Element axisheads = doc2.createElement("axis");
						axisheads.appendChild(doc2.createTextNode("1 0 0"));
						Joint.appendChild(axisheads);

						Element maxtorqueheads = doc2
								.createElement("maxtorque");
						maxtorqueheads.appendChild(doc2.createTextNode("0.4"));
						Joint.appendChild(maxtorqueheads);

						Element maxvelheads = doc2.createElement("maxvel");
						maxvelheads.appendChild(doc2.createTextNode("4.5"));
						Joint.appendChild(maxvelheads);

						Element resolutionheads = doc2
								.createElement("resolution");
						resolutionheads.appendChild(doc2.createTextNode("1.7"));
						Joint.appendChild(resolutionheads);
					}

				}
			} else if (opposable) {
				// add element to geom
				Element extents = doc2.createElement("Extents");
				// larghezza e lunghezza variano in base al numero di dita e
				// alla
				// disposizione della base
				// 0.03 * fingersnumber * fingerdistance + " 0.01 0.04"

				extents.appendChild(doc2
						.createTextNode(0.08
								* fingerdistance
								+ " 0.01 "
								+ 0.054
								* (Math.max(fingersnumber - thumbs,
										thumbs) * fingerdistance))); // lunghezza,
				// altezza,
				// larghezza
				geom.appendChild(extents);

				// add element to geom
				Element translation2 = doc2.createElement("Translation");
				translation2.appendChild(doc2.createTextNode("0.03 -0.046 "
						+ 0.02 * Math.max(fingersnumber - thumbs, thumbs)
						* fingerdistance));
				geom.appendChild(translation2);

				// add element to geom
				Element transparency = doc2.createElement("transparency");
				transparency.appendChild(doc2.createTextNode("0.4"));
				geom.appendChild(transparency);

				// define manipulator base
				Element mass = doc2.createElement("mass");
				body.appendChild(mass);

				// set attribute to body element
				Attr masstype = doc2.createAttribute("type");
				masstype.setValue("sphere");
				mass.setAttributeNode(masstype);

				// add element to mass
				Element total = doc2.createElement("total");
				total.appendChild(doc2.createTextNode(Double.toString(0.025
						* fingersnumber * fingerdistance))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(total);

				// add element to mass
				Element radius = doc2.createElement("radius");
				radius.appendChild(doc2.createTextNode(Double
						.toString(0.018 * fingersnumber))); // to multiply for
				// the number of
				// fingers
				mass.appendChild(radius);

				/*
				 * for each finger it is necessary to have a module on the base
				 * (later on we will add the thumbs)
				 */
				for (int f = 0; f < fingersnumber - thumbs; f++) {
					Element geomf = doc2.createElement("Geom");
					body.appendChild(geomf);

					// set attribute to body element
					Attr geomtypef = doc2.createAttribute("type");
					geomtypef.setValue("trimesh");
					geomf.setAttributeNode(geomtypef);

					// add element to geomf
					Element render = doc2.createElement("Render");
					render.appendChild(doc2.createTextNode(filePath
							+ " 0.001")); // to multiply for
					// the number of
					// fingers
					geomf.appendChild(render);

					// add element to geomf
					Element data = doc2.createElement("data");
					data.appendChild(doc2
							.createTextNode(filePath + " 0.001")); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(data);

					// add element to geomf
					Element rotationmat = doc2.createElement("RotationMat");
					rotationmat.appendChild(doc2
							.createTextNode("0 0 -1 0 1 0 1 0 0"));
					geomf.appendChild(rotationmat);

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb.appendChild(doc2
							.createTextNode("-0.007 -0.018 " + 0.06 * f
									* fingerdistance)); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(translationb);
				}
				// now we will add the thumbs
				for (int t = 0; t < thumbs; t++) {
					Element geomf = doc2.createElement("Geom");
					body.appendChild(geomf);

					// set attribute to body element
					Attr geomtypef = doc2.createAttribute("type");
					geomtypef.setValue("trimesh");
					geomf.setAttributeNode(geomtypef);

					// add element to geomf
					Element render = doc2.createElement("Render");
					render.appendChild(doc2.createTextNode(filePath
							+ " 0.001")); // to multiply for
					// the number of
					// fingers
					geomf.appendChild(render);

					// add element to geomf
					Element data = doc2.createElement("data");
					data.appendChild(doc2
							.createTextNode(filePath + " 0.001")); // to
					// multiply
					// for
					// the number of
					// fingers
					geomf.appendChild(data);

					// add element to geomf
					Element rotationmat = doc2.createElement("RotationMat");
					rotationmat.appendChild(doc2
							.createTextNode("0 0 1 0 1 0 -1 0 0"));
					geomf.appendChild(rotationmat);

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb.appendChild(doc2.createTextNode(0.067
							* fingerdistance + " -0.018 " + 0.06 * t
							* fingerdistance)); // to multiply for
					// the number of
					// fingers
					geomf.appendChild(translationb);
				}

				// now we add the fingers
				// for each finger that are not thumbs
				for (int f = 0; f < fingersnumber - thumbs; f++) {
					// if the f-th finger has more than one module
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						// we add modules in the middle
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]) - 1; m++) {
							// root elements
							Element fingerkinbody = doc2
									.createElement("Kinbody");
							rootElement2.appendChild(fingerkinbody);

							// set attribute to root element
							Attr prefix = doc2.createAttribute("prefix");
							prefix.setValue(Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(prefix);

							// set attribute to root element
							Attr fingerkinbodyname = doc2
									.createAttribute("name");
							fingerkinbodyname.setValue("kPY"
									+ Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(fingerkinbodyname);
							// if (Integer.parseInt(fingersconfiguration[f]) >
							// m)

							// root elements
							Element bodym = doc2.createElement("Body");
							fingerkinbody.appendChild(bodym);

							// set attribute to root element
							Attr bodymname = doc2.createAttribute("name");
							bodymname.setValue("Seg");
							bodym.setAttributeNode(bodymname);

							// set attribute to root element
							Attr bodymtype = doc2.createAttribute("type");
							bodymtype.setValue("dynamic");
							bodym.setAttributeNode(bodymtype);

							// define manipulator base
							Element massm = doc2.createElement("mass");
							bodym.appendChild(massm);

							// set attribute to body element
							Attr massmtype = doc2.createAttribute("type");
							massmtype.setValue("sphere");
							massm.setAttributeNode(massmtype);

							// add element to mass
							Element totalm = doc2.createElement("total");
							totalm.appendChild(doc2.createTextNode("0.05"));
							massm.appendChild(totalm);

							// add element to mass
							Element radiusm = doc2.createElement("radius");
							radiusm.appendChild(doc2.createTextNode("0.036"));
							massm.appendChild(radiusm);

							Element geomm = doc2.createElement("Geom");
							bodym.appendChild(geomm);

							// set attribute to body element
							Attr geomtypem = doc2.createAttribute("type");
							geomtypem.setValue("trimesh");
							geomm.setAttributeNode(geomtypem);

							// add element to geomf
							Element renderm = doc2.createElement("Render");
							renderm.appendChild(doc2
									.createTextNode(filePath2 + " 0.001"));
							geomm.appendChild(renderm);

							// add element to geomf
							Element datam = doc2.createElement("data");
							datam.appendChild(doc2.createTextNode(filePath2
									+ " 0.001"));
							geomm.appendChild(datam);

							// add element to geomf
							Element translationm = doc2
									.createElement("translation");
							translationm.appendChild(doc2
									.createTextNode("0 0.018 -0.007"));
							geomm.appendChild(translationm);

							// Y1-body
							Element geommb = doc2.createElement("Geom");
							bodym.appendChild(geommb);

							// set attribute to body element
							Attr geomtypemb = doc2.createAttribute("type");
							geomtypemb.setValue("trimesh");
							geommb.setAttributeNode(geomtypemb);

							// add element to geomf
							Element rendermb = doc2.createElement("Render");
							rendermb.appendChild(doc2
									.createTextNode(filePath + " 0.001"));
							geommb.appendChild(rendermb);

							// add element to geomf
							Element datamb = doc2.createElement("data");
							datamb.appendChild(doc2.createTextNode(filePath
									+ " 0.001"));
							geommb.appendChild(datamb);

							// add element to geomf
							Element translationmb = doc2
									.createElement("translation");
							translationmb.appendChild(doc2
									.createTextNode("0 0.054 -0.007"));
							geommb.appendChild(translationmb);

							// add element to geomf
							Element offsetfrom = doc2
									.createElement("offsetfrom");
							if (m == 0) {
								offsetfrom.appendChild(doc2
										.createTextNode("palm"));
								fingerkinbody.appendChild(offsetfrom);

							} else {
								offsetfrom.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ m));
								fingerkinbody.appendChild(offsetfrom);
							}

							// add element to geomf
							Element rotationmat = doc2
									.createElement("RotationMat");
							rotationmat.appendChild(doc2
									.createTextNode("0 0 -1 0 1 0 1 0 0"));
							fingerkinbody.appendChild(rotationmat);

							// add element to geomf
							Element translationo = doc2
									.createElement("translation");
							translationo.appendChild(doc2
									.createTextNode("-0.014 " + 0.072 * m + " "
											+ 0.06 * f * fingerdistance));
							fingerkinbody.appendChild(translationo);
						}
					}
					// otherwise we have to add the head

					// root elements
					Element headkinbody = doc2.createElement("Kinbody");
					rootElement2.appendChild(headkinbody);

					// set attribute to root element
					Attr prefixh = doc2.createAttribute("prefix");
					prefixh.setValue("h" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(prefixh);

					// set attribute to root element
					Attr headkinbodyname = doc2.createAttribute("name");
					headkinbodyname.setValue("khead" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(headkinbodyname);

					// root elements
					Element headbody = doc2.createElement("Body");
					headkinbody.appendChild(headbody);

					// set attribute to root element
					Attr headbodyname = doc2.createAttribute("name");
					headbodyname.setValue("Head");
					headbody.setAttributeNode(headbodyname);

					// set attribute to root element
					Attr headbodytype = doc2.createAttribute("type");
					headbodytype.setValue("dynamic");
					headbody.setAttributeNode(headbodytype);

					// define manipulator base
					Element headmass = doc2.createElement("mass");
					headbody.appendChild(headmass);

					// set attribute to body element
					Attr headmasstype = doc2.createAttribute("type");
					headmasstype.setValue("sphere");
					headmass.setAttributeNode(headmasstype);

					// add element to mass
					Element headtotal = doc2.createElement("total");
					headtotal.appendChild(doc2.createTextNode("0.025"));
					headmass.appendChild(headtotal);

					// add element to mass
					Element headradius = doc2.createElement("radius");
					headradius.appendChild(doc2.createTextNode("0.018"));
					headmass.appendChild(headradius);

					Element headgeom = doc2.createElement("Geom");
					headbody.appendChild(headgeom);

					// set attribute to body element
					Attr headgeomtype = doc2.createAttribute("type");
					headgeomtype.setValue("trimesh");
					headgeom.setAttributeNode(headgeomtype);

					// add element to geomf
					Element headrender = doc2.createElement("Render");
					headrender.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headrender);

					// add element to geomf
					Element headdata = doc2.createElement("data");
					headdata.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headdata);

					// add element to geomf
					Element headtranslation = doc2.createElement("translation");
					headtranslation.appendChild(doc2
							.createTextNode("0 0.018 -0.007"));
					headgeom.appendChild(headtranslation);

					// add element to geomf
					Element rotationmat = doc2.createElement("RotationMat");
					rotationmat.appendChild(doc2
							.createTextNode("0 0 -1 0 1 0 1 0 0"));
					headkinbody.appendChild(rotationmat);

					Element headtranslation2 = doc2
							.createElement("translation");
					headtranslation2.appendChild(doc2.createTextNode("-0.014 "
							+ 0.072
							* (Integer.parseInt(fingersconfiguration[f]) - 1)
							+ " " + 0.06 * f * fingerdistance));
					headkinbody.appendChild(headtranslation2);

				}

				// now we add the thumbs
				// for each thumb
				int r = 0;
				for (int f = fingersnumber - thumbs; f < fingersnumber; f++) {

					// if the f-th finger has more than one module
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						// we add modules in the middle
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]) - 1; m++) {
							// root elements
							Element fingerkinbody = doc2
									.createElement("Kinbody");
							rootElement2.appendChild(fingerkinbody);

							// set attribute to root element
							Attr prefix = doc2.createAttribute("prefix");
							prefix.setValue(Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(prefix);

							// set attribute to root element
							Attr fingerkinbodyname = doc2
									.createAttribute("name");
							fingerkinbodyname.setValue("kPY"
									+ Integer.toString(f + 1) + (m + 1));
							fingerkinbody.setAttributeNode(fingerkinbodyname);
							// if (Integer.parseInt(fingersconfiguration[f]) >
							// m)

							// root elements
							Element bodym = doc2.createElement("Body");
							fingerkinbody.appendChild(bodym);

							// set attribute to root element
							Attr bodymname = doc2.createAttribute("name");
							bodymname.setValue("Seg");
							bodym.setAttributeNode(bodymname);

							// set attribute to root element
							Attr bodymtype = doc2.createAttribute("type");
							bodymtype.setValue("dynamic");
							bodym.setAttributeNode(bodymtype);

							// define manipulator base
							Element massm = doc2.createElement("mass");
							bodym.appendChild(massm);

							// set attribute to body element
							Attr massmtype = doc2.createAttribute("type");
							massmtype.setValue("sphere");
							massm.setAttributeNode(massmtype);

							// add element to mass
							Element totalm = doc2.createElement("total");
							totalm.appendChild(doc2.createTextNode("0.05"));
							massm.appendChild(totalm);

							// add element to mass
							Element radiusm = doc2.createElement("radius");
							radiusm.appendChild(doc2.createTextNode("0.036"));
							massm.appendChild(radiusm);

							Element geomm = doc2.createElement("Geom");
							bodym.appendChild(geomm);

							// set attribute to body element
							Attr geomtypem = doc2.createAttribute("type");
							geomtypem.setValue("trimesh");
							geomm.setAttributeNode(geomtypem);

							// add element to geomf
							Element renderm = doc2.createElement("Render");
							renderm.appendChild(doc2
									.createTextNode(filePath2 + " 0.001"));
							geomm.appendChild(renderm);

							// add element to geomf
							Element datam = doc2.createElement("data");
							datam.appendChild(doc2.createTextNode(filePath2
									+ " 0.001"));
							geomm.appendChild(datam);

							// add element to geomf
							Element translationm = doc2
									.createElement("translation");
							translationm.appendChild(doc2
									.createTextNode("0 0.018 -0.007"));
							geomm.appendChild(translationm);

							// Y1-body
							Element geommb = doc2.createElement("Geom");
							bodym.appendChild(geommb);

							// set attribute to body element
							Attr geomtypemb = doc2.createAttribute("type");
							geomtypemb.setValue("trimesh");
							geommb.setAttributeNode(geomtypemb);

							// add element to geomf
							Element rendermb = doc2.createElement("Render");
							rendermb.appendChild(doc2
									.createTextNode(filePath + " 0.001"));
							geommb.appendChild(rendermb);

							// add element to geomf
							Element datamb = doc2.createElement("data");
							datamb.appendChild(doc2.createTextNode(filePath
									+ " 0.001"));
							geommb.appendChild(datamb);

							// add element to geomf
							Element translationmb = doc2
									.createElement("translation");
							translationmb.appendChild(doc2
									.createTextNode("0 0.054 -0.007"));
							geommb.appendChild(translationmb);

							// add element to geomf
							Element offsetfrom = doc2
									.createElement("offsetfrom");
							if (m == 0) {
								offsetfrom.appendChild(doc2
										.createTextNode("palm"));
								fingerkinbody.appendChild(offsetfrom);

							} else {
								offsetfrom.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ m));
								fingerkinbody.appendChild(offsetfrom);
							}

							// add element to geomf
							Element rotationmat = doc2
									.createElement("RotationMat");
							rotationmat.appendChild(doc2
									.createTextNode("0 0 1 0 1 0 -1 0 0"));
							fingerkinbody.appendChild(rotationmat);

							// add element to geomf
							Element translationb = doc2
									.createElement("translation");
							translationb.appendChild(doc2.createTextNode(0.074
									* fingerdistance + (1 - fingerdistance)
									* 0.007 + " " + 0.072 * m + "" + 0.06 * r
									* fingerdistance)); // to multiply for
							// the number of
							// fingers
							fingerkinbody.appendChild(translationb);

						}
					}
					// otherwise we have to add the head

					// root elements
					Element headkinbody = doc2.createElement("Kinbody");
					rootElement2.appendChild(headkinbody);

					// set attribute to root element
					Attr prefixh = doc2.createAttribute("prefix");
					prefixh.setValue("h" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(prefixh);

					// set attribute to root element
					Attr headkinbodyname = doc2.createAttribute("name");
					headkinbodyname.setValue("khead" + Integer.toString(f + 1));
					headkinbody.setAttributeNode(headkinbodyname);

					// root elements
					Element headbody = doc2.createElement("Body");
					headkinbody.appendChild(headbody);

					// set attribute to root element
					Attr headbodyname = doc2.createAttribute("name");
					headbodyname.setValue("Head");
					headbody.setAttributeNode(headbodyname);

					// set attribute to root element
					Attr headbodytype = doc2.createAttribute("type");
					headbodytype.setValue("dynamic");
					headbody.setAttributeNode(headbodytype);

					// define manipulator base
					Element headmass = doc2.createElement("mass");
					headbody.appendChild(headmass);

					// set attribute to body element
					Attr headmasstype = doc2.createAttribute("type");
					headmasstype.setValue("sphere");
					headmass.setAttributeNode(headmasstype);

					// add element to mass
					Element headtotal = doc2.createElement("total");
					headtotal.appendChild(doc2.createTextNode("0.025"));
					headmass.appendChild(headtotal);

					// add element to mass
					Element headradius = doc2.createElement("radius");
					headradius.appendChild(doc2.createTextNode("0.018"));
					headmass.appendChild(headradius);

					Element headgeom = doc2.createElement("Geom");
					headbody.appendChild(headgeom);

					// set attribute to body element
					Attr headgeomtype = doc2.createAttribute("type");
					headgeomtype.setValue("trimesh");
					headgeom.setAttributeNode(headgeomtype);

					// add element to geomf
					Element headrender = doc2.createElement("Render");
					headrender.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headrender);

					// add element to geomf
					Element headdata = doc2.createElement("data");
					headdata.appendChild(doc2.createTextNode(filePath2
							+ " 0.001"));
					headgeom.appendChild(headdata);

					// add element to geomf
					Element headtranslation = doc2.createElement("translation");
					headtranslation.appendChild(doc2
							.createTextNode("0 0.018 -0.007"));
					headgeom.appendChild(headtranslation);

					// add element to geomf
					Element rotationmat = doc2.createElement("RotationMat");
					rotationmat.appendChild(doc2
							.createTextNode("0 0 1 0 1 0 -1 0 0"));
					headkinbody.appendChild(rotationmat);

					// add element to geomf
					Element translationb = doc2.createElement("translation");
					translationb.appendChild(doc2.createTextNode(0.074
							* fingerdistance + (1 - fingerdistance) * 0.007
							+ " " + 0.072
							* (Integer.parseInt(fingersconfiguration[f]) - 1)
							+ "" + 0.06 * r * fingerdistance)); // to multiply
					// for
					// the number of
					// fingers
					headkinbody.appendChild(translationb);

					r++;

				}
				// joints
				int j = 1;
				for (int f = 0; f < fingersnumber; f++) {
					// if the f-th fingers has more than one dof
					// Element Joint = doc2.createElement("Joint");
					if (Integer.parseInt(fingersconfiguration[f]) > 1) {
						for (int m = 0; m < Integer
								.parseInt(fingersconfiguration[f]); m++) {
							// -1 apposta per head
							// root elements
							Element Joint = doc2.createElement("Joint");
							rootElement2.appendChild(Joint);

							// set attribute to root element
							Attr jointname = doc2.createAttribute("name");
							jointname.setValue("JF" + Integer.toString(j));
							Joint.setAttributeNode(jointname);
							j++;

							// set attribute to root element
							Attr type = doc2.createAttribute("type");
							type.setValue("hinge");
							Joint.setAttributeNode(type);
							// if it is the first joint
							if (m == 0) {
								Element jointbodypalm = doc2
										.createElement("Body");
								jointbodypalm.appendChild(doc2
										.createTextNode("palm"));
								Joint.appendChild(jointbodypalm);

								Element jointbodypalm2 = doc2
										.createElement("Body");
								jointbodypalm2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodypalm2);

								Element offsetfrompalm = doc2
										.createElement("offsetfrom");
								offsetfrompalm.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrompalm);

								Element weightpalm = doc2
										.createElement("weight");
								weightpalm.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightpalm);

								Element limitsdegpalm = doc2
										.createElement("limitsdeg");
								limitsdegpalm.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegpalm);

								Element axispalm = doc2.createElement("axis");
								axispalm.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axispalm);

								Element maxtorquepalm = doc2
										.createElement("maxtorque");
								maxtorquepalm.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquepalm);

								Element maxvelpalm = doc2
										.createElement("maxvel");
								maxvelpalm.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelpalm);

								Element resolutionpalm = doc2
										.createElement("resolution");
								resolutionpalm.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionpalm);

							} else if (m < Integer
									.parseInt(fingersconfiguration[f]) - 1) {// add
								// the
								// others
								// joints
								// in
								// the
								// middle
								Element jointbodymiddle = doc2
										.createElement("Body");
								jointbodymiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodymiddle);

								Element jointbodymiddle2 = doc2
										.createElement("Body");
								jointbodymiddle2.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(jointbodymiddle2);

								Element offsetfrommiddle = doc2
										.createElement("offsetfrom");
								offsetfrommiddle.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m + 1)
												+ "Seg"));
								Joint.appendChild(offsetfrommiddle);

								Element weightmiddle = doc2
										.createElement("weight");
								weightmiddle.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weightmiddle);

								Element limitsdegmiddle = doc2
										.createElement("limitsdeg");
								limitsdegmiddle.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdegmiddle);

								Element axismiddle = doc2.createElement("axis");
								axismiddle.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axismiddle);

								Element maxtorquemiddle = doc2
										.createElement("maxtorque");
								maxtorquemiddle.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquemiddle);

								Element maxvelmiddle = doc2
										.createElement("maxvel");
								maxvelmiddle.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelmiddle);

								Element resolutionmiddle = doc2
										.createElement("resolution");
								resolutionmiddle.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionmiddle);
							}
							// joint head
							if (m == Integer.parseInt(fingersconfiguration[f]) - 1) {
								// attacca head
								Element jointbodyhead = doc2
										.createElement("Body");
								jointbodyhead.appendChild(doc2
										.createTextNode(Integer.toString(f + 1)
												+ Integer.toString(m) + "Seg"));
								Joint.appendChild(jointbodyhead);

								Element jointbodyhead2 = doc2
										.createElement("Body");
								jointbodyhead2.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(jointbodyhead2);

								Element offsetfromhead = doc2
										.createElement("offsetfrom");
								offsetfromhead.appendChild(doc2
										.createTextNode("h"
												+ Integer.toString(f + 1)
												+ "Head"));
								Joint.appendChild(offsetfromhead);

								Element weighthead = doc2
										.createElement("weight");
								weighthead.appendChild(doc2
										.createTextNode("0.14894"));
								Joint.appendChild(weighthead);

								Element limitsdeghead = doc2
										.createElement("limitsdeg");
								limitsdeghead.appendChild(doc2
										.createTextNode("-90 90"));
								Joint.appendChild(limitsdeghead);

								Element axishead = doc2.createElement("axis");
								axishead.appendChild(doc2
										.createTextNode("1 0 0"));
								Joint.appendChild(axishead);

								Element maxtorquehead = doc2
										.createElement("maxtorque");
								maxtorquehead.appendChild(doc2
										.createTextNode("0.4"));
								Joint.appendChild(maxtorquehead);

								Element maxvelhead = doc2
										.createElement("maxvel");
								maxvelhead.appendChild(doc2
										.createTextNode("4.5"));
								Joint.appendChild(maxvelhead);

								Element resolutionhead = doc2
										.createElement("resolution");
								resolutionhead.appendChild(doc2
										.createTextNode("1.7"));
								Joint.appendChild(resolutionhead);
							}

						}

					} else {

						Element Joint = doc2.createElement("Joint");
						rootElement2.appendChild(Joint);

						// set attribute to root element
						Attr jointname = doc2.createAttribute("name");
						jointname.setValue("JF" + Integer.toString(j));
						Joint.setAttributeNode(jointname);
						j++;

						// set attribute to root element
						Attr type = doc2.createAttribute("type");
						type.setValue("hinge");
						Joint.setAttributeNode(type);
						// if it is the first joint

						Element jointbodyheads = doc2.createElement("Body");
						jointbodyheads.appendChild(doc2.createTextNode("palm"));
						Joint.appendChild(jointbodyheads);

						Element jointbodyhead2s = doc2.createElement("Body");
						jointbodyhead2s.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(jointbodyhead2s);

						Element offsetfromheads = doc2
								.createElement("offsetfrom");
						offsetfromheads.appendChild(doc2.createTextNode("h"
								+ Integer.toString(f + 1) + "Head"));
						Joint.appendChild(offsetfromheads);

						Element weightheads = doc2.createElement("weight");
						weightheads.appendChild(doc2.createTextNode("0.14894"));
						Joint.appendChild(weightheads);

						Element limitsdegheads = doc2
								.createElement("limitsdeg");
						limitsdegheads.appendChild(doc2
								.createTextNode("-90 90"));
						Joint.appendChild(limitsdegheads);

						Element axisheads = doc2.createElement("axis");
						axisheads.appendChild(doc2.createTextNode("1 0 0"));
						Joint.appendChild(axisheads);

						Element maxtorqueheads = doc2
								.createElement("maxtorque");
						maxtorqueheads.appendChild(doc2.createTextNode("0.4"));
						Joint.appendChild(maxtorqueheads);

						Element maxvelheads = doc2.createElement("maxvel");
						maxvelheads.appendChild(doc2.createTextNode("4.5"));
						Joint.appendChild(maxvelheads);

						Element resolutionheads = doc2
								.createElement("resolution");
						resolutionheads.appendChild(doc2.createTextNode("1.7"));
						Joint.appendChild(resolutionheads);
					}

				}

			}

			// write the content into xml file
			TransformerFactory transformerFactory2 = TransformerFactory
					.newInstance();
			Transformer transformer2 = transformerFactory2.newTransformer();
			DOMSource source2 = new DOMSource(doc2);
			StreamResult result2 = new StreamResult(new File(
					"barretthand.kinbody.xml"));
			transformer2.transform(source2, result2);

			System.out.println("Done (kinbody.xml)");
			// ##############################
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
