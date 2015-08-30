package com.panayotis.awt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import com.panayotis.awt.IntFieldListener;

import com.panayotis.wt.wTextField;
import com.panayotis.wt.wPanel;
import com.panayotis.wt.wButton;

public class IntField extends wPanel
{
	private IntFieldListener listener;

	private wTextField tf;
	private int minimum;
	private int maximum;
	private int smallstep;
	private int bigstep;
	private int current;

	public final static int ABSOLUT_MIN = -32565;
	public final static int ABSOLUT_MAX = 32565;
	public final static int ABSOLUT_STEP = 10;


	public IntField ()
	{
		this ( ABSOLUT_MIN, ABSOLUT_MAX, 1, ABSOLUT_STEP, 0);
	}

	public IntField (int v_min, int v_max, int v_small, int v_big, int v_cur )
	{
		wButton prev, f_prev, next, f_next, zero, first, last;
		wPanel p_but;

		p_but = new wPanel();
		tf = new wTextField();
		prev = new wButton ("<");
		f_prev = new wButton ("<<");
		next = new wButton (">");
		f_next = new wButton (">>");
		zero = new wButton (" 0 ");
		first = new wButton ("|<");
		last = new wButton (">|");

		setLayout ( new BorderLayout () );
		p_but.setLayout ( new GridLayout (1, 7));
		tf.setEditable (false);

		p_but.add (first);
		p_but.add (f_prev);
		p_but.add (prev);
		p_but.add (zero);
		p_but.add (next);
		p_but.add (f_next);
		p_but.add (last);

		add ( "Center", tf );
		add ( "East", p_but);

		first.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				first_click ();
			}
		});
		f_prev.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				f_prev_click ();
			}
		});
		prev.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				prev_click ();
			}
		});
		zero.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				zero_click ();
			}
		});
		next.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				next_click ();
			}
		});
		f_next.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				f_next_click ();
			}
		});
		last.addActionListener ( new ActionListener () {
			public void actionPerformed ( ActionEvent e ) {
				last_click ();
			}
		});

		minimum = v_min;
		maximum = v_max;
		smallstep = v_small;
		bigstep = v_big;
		updateData( v_cur );
	}

	private void first_click ()
	{
		updateData ( minimum );
	}

	private void f_prev_click ()
	{
		updateData ( current - bigstep);
	}

	private void prev_click ()
	{
		updateData ( current - smallstep);
	}

	private void zero_click ()
	{
		updateData ( 0 );
	}

	private void next_click ()
	{
		updateData ( current + smallstep);
	}

	private void f_next_click ()
	{
		updateData ( current + bigstep);
	}

	private void last_click ()
	{
		updateData ( maximum );
	}


	public void updateData (int value)
	{
		if ( value >= minimum && value <= maximum ) {
			current = value;
		}
		else if ( maximum < value ) {
			current =  maximum;
		}
		else if ( minimum > value ) {
			current =  minimum;
		}
		else {
			System.err.println ( "Errir in IntField. Please report these numbers to teras@writeme.com  :");
			System.err.println ( "min=" + minimum + " max=" + maximum + " val=" + value );
		}
		tf.setText ( String.valueOf (current));
		if ( listener != null ) {
			listener.valueChanged ( this );
		}
	}

	public void setSmallStep ( int value )
	{
		if ( value > 0 && value <= bigstep ) {
			smallstep = value;
		}
	}

	public void setBigStep ( int value )
	{
		if ( value >= smallstep && value <= ( maximum - minimum -1) ) {
	 		bigstep = value;
		}
	}

	public void setMinimum ( int value )
	{
		if ( value >= ABSOLUT_MIN) {
			if ( value < maximum ) {
				minimum = value;
			}
			else {
				minimum = maximum;
			}
		}
	}

	public void setMaximum ( int value )
	{
		if ( value <= ABSOLUT_MAX) {
			if ( value > minimum ) {
				maximum = value;
			}
			else {
				maximum = minimum;
			}
		}
	}

	public int getInt ()
	{
		return current;
	}

	public int getMinimum ()
	{
		return minimum;
	}

	public int getMaximum ()
	{
		return maximum;
	}

	public int getSmallStep ()
	{
		return smallstep;
	}

	public int getBigStep ()
	{
		return bigstep;
	}

	public void setIntFieldListener ( IntFieldListener l)
	{
		listener = l;
	}
}
