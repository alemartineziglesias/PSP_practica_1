package practica;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Practica extends JFrame 
{
    private static final long serialVersionUID = 1L;
    private static String[] listaArchivos = new String[100000];
    private static int cantidadArchivos = 0;

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    Practica ventana = new Practica();
                    ventana.setVisible(true);
                } 
                catch(Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public Practica() 
    {
        //Frame
        setTitle("Mis ficheros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        
        //Panel
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);

        //Lista
        JList<String> directorios = new JList<>(listaArchivos);
        JScrollPane scroll = new JScrollPane(directorios);
        scroll.setBounds(10, 11, 414, 152);
        panel.add(scroll);
        directorios.addMouseListener(new MouseAdapter() 
        {
            @SuppressWarnings("deprecation")
			@Override
            public void mouseClicked(MouseEvent e) 
            {
            	if (e.getClickCount() == 2) 
            	{
					try
					{
						Runtime.getRuntime().exec(directorios.getSelectedValue());
					} 
					catch (IOException ioe)
					{
						ioe.printStackTrace();
					}
				}
            }
        });

        //Barra de texto
        JTextField textField = new JTextField();
        textField.setBounds(138, 172, 162, 30);
        panel.add(textField);
        
        //Botón
        JButton buscar = new JButton("Buscar");
        buscar.setBounds(174, 214, 89, 35);
        panel.add(buscar);
        buscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	cantidadArchivos = 0;
                for(int i = 0; i < listaArchivos.length; i++)
                {
                	listaArchivos[i] = null;
                }
                if(textField.getText().isBlank()) 
                {
                	listaArchivos[0] = "Escriba la extensión del tipo de archivos que quiere buscar";
                    directorios.setListData(listaArchivos);
                } 
                else if(textField.getText().charAt(0) == '.') 
                {
                	listaArchivos[0] = "Ponga solo la extension (exe, txt, etc...)";
                    directorios.setListData(listaArchivos);
                } 
                else 
                {
                    String extension = textField.getText();
                    File[] unidades = File.listRoots();
                    for (File unidad : unidades) 
                    {
                        encontrarArchivos(unidad, extension.toLowerCase().trim());
                        directorios.setListData(listaArchivos);
                    }
                }
            }
        });
        
    }

    public static void encontrarArchivos(File directorio, String extension) 
    {
        File[] archivos = directorio.listFiles();

        if(archivos != null) 
        {
            for(File archivo : archivos) 
            {
                if(archivo.isDirectory()) 
                {
                    encontrarArchivos(archivo, extension);
                } 
                else if(archivo.isFile() && archivo.getName().toLowerCase().endsWith("." + extension)) 
                {
                	listaArchivos[cantidadArchivos++] = archivo.getAbsolutePath().toLowerCase();
                }
            }
        }
        if(cantidadArchivos == 0) 
        {
        	listaArchivos[0] = "No se ha encontrado ningún archivo con esa extensión";
        } 
    }
}