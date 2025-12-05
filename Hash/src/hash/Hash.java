package hash;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Date;
/**
 *
 * @author marye
 */
public class Hash {
    private static final Color BLUE_BG = new Color(0, 74, 173);
    private static final Color SKY_TEXT = new Color(110, 207, 255);
    private static final Color GRAY_BOX = new Color(196, 196, 196);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
          SwingUtilities.invokeLater(()-> {
              try{
                  String nombreBase="Users";
                  PSNUsers manejo = new PSNUsers(nombreBase);
                  
                  JFrame window = new JFrame("PSN Manejo");
                  window.setSize(900, 650);
                  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                  window.setLocationRelativeTo(null);
                  
                  CardLayout layout= new CardLayout();
                  JPanel cards = new JPanel(layout);
                  
                  JPanel addUserPanel = buildAddUserPanel(manejo, window);
                  JPanel deleteUserPanel= buildDeleteUserPanel(manejo, window);
                  JPanel addTrophyPanel = buildAddTrophyPanel(manejo, window, nombreBase);
                  JPanel infoPanel = buildInfoPanel(window, nombreBase);
                  
                  
                  cards.add(addUserPanel, "add");
                  cards.add(deleteUserPanel, "delete");
                  cards.add(addTrophyPanel, "trophy");
                  cards.add(infoPanel, "info");
                  
                  JPanel nav= new JPanel(new GridLayout(1, 4));
                  nav.add(navButton("AGREGAR", "add", layout, cards));
                  nav.add(navButton("BORRAR", "delete", layout, cards));
                  nav.add(navButton("TROFEO", "trophy", layout, cards));
                  nav.add(navButton("INFO", "info", layout, cards));
                  
                  window.add(nav, BorderLayout.NORTH);
                  window.add(cards, BorderLayout.CENTER);
                  
                  window.setVisible(true);
                      
              }catch(IOException ex){
                  JOptionPane.showMessageDialog(null, "Error inicializando PSNUsers: "+ex.getMessage());
              }
          });
    }
    
    private static JButton navButton(String title, String panel, CardLayout layout, JPanel cards){
        JButton b = new JButton(title);
        b.setBackground(GRAY_BOX);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.addActionListener(e -> layout.show(cards, panel));
        return b;
    }
    
    private static JPanel buildAddUserPanel(PSNUsers manejo, JFrame parent){
        JPanel p= new JPanel(null);
        p.setBackground(BLUE_BG);
        
        JLabel l= new JLabel("USUARIO");
        l.setBounds(70, 40, 600, 60);
        l.setForeground(SKY_TEXT);
        l.setFont(new Font("Arial", Font.PLAIN, 24));
        p.add(l);
        
        JTextField txtUser= new JTextField();
        txtUser.setBounds(70, 130, 700, 60);
        txtUser.setBackground(GRAY_BOX);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 24));
        p.add(txtUser);
        
        
        JButton btn= new JButton("AGREGAR");
        btn.setBounds(70, 230, 180, 60);
        btn.setBackground(GRAY_BOX);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(btn);
        
        btn.addActionListener(e -> {
            try{
                String usuario= txtUser.getText().trim();
                if(usuario.isEmpty()){
                    JOptionPane.showMessageDialog(parent, "Debe ingresar un ususario.");
                    return;
                }
                manejo.addUser(usuario);
                JOptionPane.showMessageDialog(parent, "Usuario agregado.");
                txtUser.setText("");
            }catch(IOException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error: "+ex.getMessage());
            }
        });
        
        return p;
    }
    
    private static JPanel buildDeleteUserPanel(PSNUsers manejo, JFrame parent){
        JPanel p= new JPanel(null);
        p.setBackground(BLUE_BG);
        
        JLabel l= new JLabel("USUARIO PARA BORRAR");
        l.setBounds(70, 40, 600, 60);
        l.setForeground(SKY_TEXT);
        l.setFont(new Font("Arial", Font.PLAIN, 24));
        p.add(l);
        
        JTextField txtUser= new JTextField();
        txtUser.setBounds(70, 130, 700, 60);
        txtUser.setBackground(GRAY_BOX);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 24));
        p.add(txtUser);
        
        
        JButton btn= new JButton("BORRAR");
        btn.setBounds(70, 230, 180, 60);
        btn.setBackground(GRAY_BOX);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(btn);
        
        btn.addActionListener(e -> {
            try {
                String usuario = txtUser.getText().trim();

                if (usuario.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "Debe ingresar un usuario.");
                    return;
                }

                if(!userExistsAndActive("Users", usuario)) {
                    JOptionPane.showMessageDialog(parent, "El usuario no existe o ya está inactivo.");
                    return;
                }

                manejo.deactivateUser(usuario);
                JOptionPane.showMessageDialog(parent, "Usuario desactivado con éxito.");

                txtUser.setText("");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
            }
        });
   
        return p;
    }
    
    private static JPanel buildAddTrophyPanel(PSNUsers manejo, JFrame parent, String nombreBase){
        JPanel p= new JPanel(null);
        p.setBackground(BLUE_BG);

        JLabel u= bigLabel("USUARIO:", 70, 40);
        JLabel g= bigLabel("JUEGO:", 70, 140);
        JLabel t= bigLabel("TROFEO:", 70, 240);
        JLabel tipo= bigLabel("TIPO:", 70, 340);

        p.add(u);
        p.add(g);
        p.add(t);
        p.add(tipo);

        JTextField txtUser= bigField(350, 40);
        JTextField txtGame= bigField(350, 140);
        JTextField txtTro= bigField(350, 240);

        p.add(txtUser);
        p.add(txtGame);
        p.add(txtTro);

        // COMBOBOX TIPOS
        JComboBox<Trophy> cbTypes = new JComboBox<>(Trophy.values());
        cbTypes.setBounds(350, 340, 250, 60);
        cbTypes.setBackground(GRAY_BOX);
        cbTypes.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(cbTypes);

        // BOTÓN IMAGEN
        JButton imgBtn= new JButton("IMAGEN");
        imgBtn.setBounds(620, 340, 160, 60);
        imgBtn.setBackground(GRAY_BOX);
        imgBtn.setFont(new Font("Arial", Font.BOLD, 18));
        p.add(imgBtn);

        final byte[][] imgBytes= new byte[1][];

        imgBtn.addActionListener(ev ->{
            try{
                JFileChooser fc = new JFileChooser();
                if(fc.showOpenDialog(parent)==JFileChooser.APPROVE_OPTION){
                    File f= fc.getSelectedFile();
                    imgBytes[0]= Files.readAllBytes(f.toPath());
                    imgBtn.setText("LISTA");
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(parent, "Error al cargar imagen.");
            }
        });

        JButton btn= new JButton("AGREGAR");
        btn.setBounds(350, 430, 200, 60);
        btn.setBackground(GRAY_BOX);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(btn);

        btn.addActionListener(ev ->{
            try{
                String usuario= txtUser.getText().trim();
                String game= txtGame.getText().trim();
                String troName= txtTro.getText().trim();
                Trophy tipoTrofeo= (Trophy) cbTypes.getSelectedItem();

                if(usuario.isEmpty() || game.isEmpty() || troName.isEmpty() || tipoTrofeo==null || imgBytes[0]==null){
                    JOptionPane.showMessageDialog(parent, "Debe completar todos los campos y cargar una imagen.");
                    return;
                }

                if(!userExistsAndActive(nombreBase, usuario)){
                    JOptionPane.showMessageDialog(parent, "El usuario no existe o está inactivo.");
                    return;
                }

                manejo.addTrophyTo(usuario, game, troName, tipoTrofeo, imgBytes[0]);
                JOptionPane.showMessageDialog(parent, "Trofeo agregado correctamente.");

                txtUser.setText("");
                txtGame.setText("");
                txtTro.setText("");
                cbTypes.setSelectedIndex(0);
                imgBytes[0]=null;
                imgBtn.setText("IMAGEN");

            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error: "+ex.getMessage());
            }
        });

        return p;
    }

    
    private static JPanel buildInfoPanel(JFrame parent, String baseName) {
        JPanel p = new JPanel(null);
        p.setBackground(BLUE_BG);

        JLabel l = new JLabel("USERNAME:");
        l.setBounds(70, 30, 400, 60);
        l.setForeground(SKY_TEXT);
        l.setFont(new Font("Arial", Font.BOLD, 40));
        p.add(l);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(70, 110, 450, 50);
        txtUser.setBackground(GRAY_BOX);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 22));
        p.add(txtUser);

        JButton btn = new JButton("BUSCAR");
        btn.setBounds(550, 110, 180, 50);
        btn.setBackground(GRAY_BOX);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        p.add(btn);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BLUE_BG);

        JScrollPane sp = new JScrollPane(content);
        sp.setBounds(70, 190, 750, 380);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        p.add(sp);

        btn.addActionListener(ev -> {
            try {
                content.removeAll();
                String username = txtUser.getText().trim();

                if (username.isEmpty()) {
                    JLabel msg = new JLabel("Debe ingresar un username.");
                    msg.setForeground(Color.WHITE);
                    msg.setFont(new Font("Arial", Font.BOLD, 24));
                    content.add(msg);
                    content.revalidate();
                    content.repaint();
                    return;
                }

                File f = new File(baseName + ".psn");
                if (!f.exists()) {
                    JLabel msg = new JLabel("No hay usuarios registrados.");
                    msg.setForeground(Color.WHITE);
                    msg.setFont(new Font("Arial", Font.BOLD, 24));
                    content.add(msg);
                    content.revalidate();
                    content.repaint();
                    return;
                }

                int puntos = 0;
                int trofeos = 0;
                boolean found = false;

                try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
                    raf.seek(0);
                    while (raf.getFilePointer() < raf.length()) {
                        long pos = raf.getFilePointer();
                        String user = readFixed(raf, 50);
                        int pts = raf.readInt();
                        int trs = raf.readInt();
                        boolean activo = raf.readBoolean();

                        if (user.equals(username) && activo) {
                            puntos = pts;
                            trofeos = trs;
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    JLabel msg = new JLabel("Usuario no encontrado o inactivo.");
                    msg.setForeground(Color.WHITE);
                    msg.setFont(new Font("Arial", Font.BOLD, 24));
                    content.add(msg);
                    content.revalidate();
                    content.repaint();
                    return;
                }

                JLabel infoUser = new JLabel(
                        "<html><span style='color:white;font-size:18px;'>" +
                                "Usuario: " + username + "<br>" +
                                "Puntos: " + puntos + "<br>" +
                                "Trofeos: " + trofeos +
                                "</span></html>"
                );
                infoUser.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(infoUser);
                content.add(Box.createVerticalStrut(15));

                File ft = new File("trophies.psn");
                if (!ft.exists()) {
                    JLabel msg = new JLabel("Este usuario no tiene trofeos registrados.");
                    msg.setForeground(Color.WHITE);
                    msg.setFont(new Font("Arial", Font.BOLD, 18));
                    content.add(msg);
                    content.revalidate();
                    content.repaint();
                    return;
                }

                try (RandomAccessFile rtrophy = new RandomAccessFile(ft, "r")) {
                    rtrophy.seek(0);
                    boolean any = false;

                    while (rtrophy.getFilePointer() < rtrophy.length()) {

                        String user = readFixed(rtrophy, 50);
                        String tipo = readFixed(rtrophy, 20);
                        String juego = readFixed(rtrophy, 100);
                        String nombre = readFixed(rtrophy, 100);
                        long fecha = rtrophy.readLong();
                        int tam = rtrophy.readInt();
                        byte[] img = new byte[tam];
                        rtrophy.readFully(img);

                        if (!user.equals(username)) {
                            continue;
                        }

                        any = true;

                        JPanel troPanel = new JPanel(null);
                        troPanel.setPreferredSize(new Dimension(700, 180));
                        troPanel.setBackground(BLUE_BG);

                        ImageIcon icon = new ImageIcon(img);
                        Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        JLabel imgLbl = new JLabel(new ImageIcon(scaled));
                        imgLbl.setBounds(10, 10, 150, 150);
                        troPanel.add(imgLbl);

                        String textHtml =
                                "<html><span style='color:white;font-size:16px;'>" +
                                        "Fecha: " + new Date(fecha) + "<br>" +
                                        "Tipo: " + tipo + "<br>" +
                                        "Juego: " + juego + "<br>" +
                                        "Trofeo: " + nombre +
                                        "</span></html>";

                        JLabel txtLbl = new JLabel(textHtml);
                        txtLbl.setBounds(180, 10, 500, 150);
                        troPanel.add(txtLbl);

                        troPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        content.add(troPanel);
                        content.add(Box.createVerticalStrut(10));
                    }

                    if (!any) {
                        JLabel msg = new JLabel("Este usuario no tiene trofeos registrados.");
                        msg.setForeground(Color.WHITE);
                        msg.setFont(new Font("Arial", Font.BOLD, 18));
                        content.add(msg);
                    }
                }

                content.revalidate();
                content.repaint();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
            }
        });

        return p;
    }

    private static boolean userExistsAndActive(String baseName, String username) {
        File f = new File(baseName + ".psn");
        if (!f.exists()) return false;

        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            raf.seek(0);

            while (raf.getFilePointer() < raf.length()) {

                String user = readFixed(raf, 50);
                int pts = raf.readInt();
                int trs = raf.readInt();
                boolean activo = raf.readBoolean();

                if (user.equals(username) && activo) {
                    return true;
                }
            }
        } catch (IOException e) {}

        return false;
    }


    private static JLabel bigLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 300, 60);
        l.setForeground(SKY_TEXT);
        l.setFont(new Font("Arial", Font.BOLD, 40));
        return l;
    }

    private static JTextField bigField(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 430, 60);
        f.setBackground(GRAY_BOX);
        f.setFont(new Font("Arial", Font.PLAIN, 24));
        return f;
    }

    private static String readFixed(RandomAccessFile raf, int tam) throws IOException {
        byte[] buffer = new byte[tam];
        raf.readFully(buffer);
        return new String(buffer, "UTF-8").trim().replace("\0", "");
    }
}

