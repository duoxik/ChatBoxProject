package com.duoxik.chatbox.client.view.gui;

import com.duoxik.chatbox.client.view.ClientGuiView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {

    private final JMenuItem itemDisconnect;

    public MenuBar(ClientGuiView view) {

        JMenu connectionsMenu = new JMenu("Connections");
        JMenuItem itemConnect = new JMenuItem("Connect");
        itemDisconnect = new JMenuItem("Disconnect");
        JMenuItem itemQuit = new JMenuItem("Quit");
        connectionsMenu.add(itemConnect);
        connectionsMenu.add(itemDisconnect);
        connectionsMenu.add(itemQuit);

        JMenu bookmarksMenu = new JMenu("Bookmarks");
        JMenuItem itemAddToBookmarks = new JMenuItem("Add to Bookmarks");
        bookmarksMenu.add(itemAddToBookmarks);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem itemAbout = new JMenuItem("About ChatBox v1.0");
        helpMenu.add(itemAbout);

        connectionsMenu.setFont(new Font(Font.SANS_SERIF, 0, 12));
        bookmarksMenu.setFont(new Font(Font.SANS_SERIF, 0, 12));
        helpMenu.setFont(new Font(Font.SANS_SERIF, 0, 12));

        itemConnect.setFont(new Font(Font.SANS_SERIF, 0, 11));
        itemDisconnect.setFont(new Font(Font.SANS_SERIF, 0, 11));
        itemQuit.setFont(new Font(Font.SANS_SERIF, 0, 11));
        itemAbout.setFont(new Font(Font.SANS_SERIF, 0, 11));
        itemAddToBookmarks.setFont(new Font(Font.SANS_SERIF, 0, 11));

        itemDisconnect.setEnabled(false);

        itemConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showConnectFrame();
            }
        });

        itemDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.abortConnection();
            }
        });

        itemQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.abortConnection();
                System.exit(0);
            }
        });

        setBackground(new Color(239, 235, 231));

        add(connectionsMenu);
        add(bookmarksMenu);
        add(helpMenu);
    }

    public void disableItemDisconnect() {
        itemDisconnect.setEnabled(false);
    }

    public void enableItemDisconnect() {
        itemDisconnect.setEnabled(true);
    }
}