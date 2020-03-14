package osm.surveyor.matchtime.gui.restamp;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import osm.surveyor.matchtime.gui.ReStamp;
import static osm.surveyor.matchtime.gui.ReStamp.createImageIcon;
import static osm.surveyor.matchtime.gui.ReStamp.i18n;
import osm.surveyor.matchtime.gui.PanelAction;
import osm.surveyor.matchtime.gui.ParameterPanelTime;

/**
 * [基準画像（開始）]選択パネル
 * @author yuu
 */
public class DialogCorectTime extends JDialog implements PanelAction {
	private static final long serialVersionUID = -8401502593882913338L;
	public JPanel mainPanel;
    ParameterPanelTime arg_basetime;	// 開始画像の基準時刻(parent)
    ParameterPanelTime basetime;	// 開始画像の基準時刻(tempolarry)
    java.awt.Button closeButton;
    JButton expandButton;
    JButton zoomInButton;
    JButton zoomOutButton;
    JLabel imageLabel;		// 開始画像の基準時刻画像表示
    JScrollPane imageSPane;	// スクロールパネル
    
    /**
     * コンストラクタ
     * @param arg3_basetime       開始画像の基準時刻:
     * @param owner
     */
    public DialogCorectTime(ParameterPanelTime arg3_basetime, Window owner) {
        super(owner, ReStamp.i18n.getString("tab.restamp.300"), Dialog.ModalityType.DOCUMENT_MODAL);
        this.arg_basetime = arg3_basetime;
        
        // INIT_CONTROLS
        setLayout(new BorderLayout());
        setSize(
            getInsets().left + getInsets().right + 720,
            getInsets().top + getInsets().bottom + 480
        );

        //---- CENTER -----
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        //---- CENTER.NORTH -----
        JPanel argsPanel;		// パラメータ設定パネル	(上部)
        argsPanel = new JPanel();
        argsPanel.setLayout(new GridLayout(2, 1));
        
        // 3. 正確な撮影時刻を入力してください。
        //    カメラの時計が正確ならば、設定を変更する必要はありません。
        JLabel label3 = new JLabel();
        label3.setText(i18n.getString("label.300"));
        argsPanel.add(label3);

        basetime = new ParameterPanelTime(
                arg_basetime.argLabel.getText(),
                "",
                arg_basetime.getImageFile()
        );
        basetime.updateButton.setVisible(false);
        basetime.resetButton.setVisible(true);
        argsPanel.add(basetime);
        centerPanel.add(argsPanel, BorderLayout.NORTH);

        //---- CENTER.CENTER -----
        // 参考画像
        imageLabel = new JLabel();
        imageSPane = new JScrollPane(imageLabel);
        centerPanel.add(imageSPane, BorderLayout.CENTER);

        //---- CENTER.SOUTH -----
        // 画像ファイル選択ダイアログを起動するボタン
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        expandButton = new JButton(createImageIcon("/images/Fit16.gif"));
        buttonPanel.add(expandButton);
        zoomInButton = new JButton(createImageIcon("/images/ZoomIn16.gif"));
        buttonPanel.add(zoomInButton);
        zoomOutButton = new JButton(createImageIcon("/images/ZoomOut16.gif"));
        buttonPanel.add(zoomOutButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        //---- SOUTH -----
        closeButton = new java.awt.Button();
        closeButton.setLabel(i18n.getString("button.close") );
        closeButton.setBounds(145,65,66,27);
        add(closeButton, BorderLayout.SOUTH);

        // 選択された画像ファイルを表示する
        imageView_Action();
        
        //{{REGISTER_LISTENERS
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
        SymAction lSymAction = new SymAction();
        closeButton.addActionListener(lSymAction);
        expandButton.addActionListener(lSymAction);
        zoomInButton.addActionListener(lSymAction);
        zoomOutButton.addActionListener(lSymAction);
        //}}
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
    {
        @Override
        public void windowClosing(java.awt.event.WindowEvent event) {
            Object object = event.getSource();
            if (object == DialogCorectTime.this) {
                dialog_WindowClosing();
            }
        }
    }
    
    class SymAction implements java.awt.event.ActionListener
    {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == closeButton) {
                dialog_WindowClosing();
            }
            else if (object == expandButton) {
            	imageView_Action();
            }
            else if (object == zoomInButton) {
            	zoomin_Action();
            }
            else if (object == zoomOutButton) {
            	zoomout_Action();
            }
        }
    }
    
    ImageIcon refImage;

    /**
     * 選択された画像ファイルを表示する
     * 基準画像ボタンがクリックされた時に、基準時刻フィールドに基準画像の作成日時を設定する。
     */
    public void imageView_Action() {
        try {
            String path = basetime.getImageFile().getImageFile().getAbsolutePath();

            // View Image File
            int size_x = imageSPane.getWidth() - 8;
            ImageIcon tmpIcon = new ImageIcon(path);
            refImage = tmpIcon;
            if (tmpIcon.getIconWidth() > size_x) {
                refImage = new ImageIcon(tmpIcon.getImage().getScaledInstance(size_x, -1, Image.SCALE_DEFAULT));
            }
            imageLabel.setIcon(refImage);
        }
        catch(NullPointerException e) {
            // 何もしない
        }
        repaint();
    }
    
    public void zoomin_Action() {
    	if (refImage != null) {
            int size_x = imageLabel.getWidth();
            String path = basetime.getImageFile().getImageFile().getAbsolutePath();
            ImageIcon tmpIcon = new ImageIcon(path);
            refImage = new ImageIcon(tmpIcon.getImage().getScaledInstance(size_x * 2, -1, Image.SCALE_DEFAULT));
            imageLabel.setIcon(refImage);
            repaint();
    	}
    }

    public void zoomout_Action() {
    	if (refImage != null) {
            int size_x = imageLabel.getWidth();
            ImageIcon tmpIcon = refImage;
            refImage = new ImageIcon(tmpIcon.getImage().getScaledInstance(size_x / 2, -1, Image.SCALE_DEFAULT));
            imageLabel.setIcon(refImage);
            repaint();
    	}
    }
    
    /**
     * ダイアログが閉じられるときのアクション
     */
    void dialog_WindowClosing() {
        String workStr = basetime.getText();
        arg_basetime.setText(workStr);
        dispose();
    }

    @Override
    public void setVisible(boolean b) {
        if(b) {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();
            setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
            bounds.y + (bounds.height - abounds.height)/2);
        }
        super.setVisible(b);
    }

    @Override
    public void openAction() {
       ; // 何もしない
    }

    /**
     *  入力条件が満たされているかどうか
     * @return
     */
    @Override
    public boolean isEnable() {
       return this.basetime.isEnable();
    }
}
