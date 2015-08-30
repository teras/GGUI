package com.panayotis.wizard;

import java.awt.Container;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Insets;

/**
* This is the layout manager for the wizard. Each element appears in a seperate line, which fully occupies it.<BR>
* Thanks to JavaSoft for the original idea.
*/
public class WizardLayout
    implements LayoutManager
{

    public WizardLayout()
    {
        this(0);
    }

    public WizardLayout(int i)
    {
        vgap = i;
    }

    public void addLayoutComponent(String s, Component component)
    {
    }

    public void removeLayoutComponent(Component component)
    {
    }

    public Dimension minimumLayoutSize(Container container)
    {
        Dimension dimension = new Dimension();
        int i = container.getComponentCount();
        for(int j = 0; j < i; j++)
        {
            Component component = container.getComponent(j);
            if(component.isVisible())
            {
                Dimension dimension1 = component.getMinimumSize();
                dimension.width = Math.max(dimension1.width, dimension.width);
                dimension.height += dimension1.height + vgap;
            }
        }

        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    public Dimension preferredLayoutSize(Container container)
    {
        Dimension dimension = new Dimension();
        int i = container.getComponentCount();
        for(int j = 0; j < i; j++)
        {
            Component component = container.getComponent(j);
            Dimension dimension1 = component.getPreferredSize();
            dimension.width = Math.max(dimension1.width, dimension.width);
            dimension.height += dimension1.height + vgap;
        }

        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    public void layoutContainer(Container container)
    {
        Insets insets = container.getInsets();
        int i = insets.top;
        container.getSize();
        int j = insets.left;
        int k = container.getSize().width - insets.right;
        int l = container.getComponentCount();
        for(int i1 = 0; i1 < l; i1++)
        {
            Component component = container.getComponent(i1);
            if(component.isVisible())
            {
                int j1 = component.getSize().height;
                component.setSize(k - j, j1);
                Dimension dimension = component.getPreferredSize();
                component.setBounds(j, i, k - j, dimension.height);
                i += dimension.height + vgap;
            }
        }

    }

    public String toString()
    {
        return getClass().getName() + "[vgap=" + vgap + "]";
    }

    int vgap;
}
