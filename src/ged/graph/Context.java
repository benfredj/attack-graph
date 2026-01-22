package ged.graph;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {
	  /**
	     Set the DOT output file format.  E.g., <tt>ps</tt> for
	     PostScript, <tt>png</tt> for PNG, etc.
	   */
	  public String outputFormat = "png";

	  /**
	     The name of the output file is derived from
	     <code>baseFileName</code> by appending successive integers.
	   */
	  public String baseFileName = "graph-";
	  private static int nextGraphNumber = 0;

	  private String nextFileName( ) {
	    return baseFileName + nextGraphNumber++ + "." + outputFormat;
	  }


	  /**
	     If null (the default), the DOT file is written to a temporary
	     file which is then deleted.<p/>

	     If non-null, the DOT file is left in this file.  This is only
	     really useful for debugging Graphviz.java.<p/>
	  */
	  public String keepDotFile = null;

	  /**
	     Set the DOT attributes for a class.  This allows you to change the
	     appearance of certain nodes in the output, but requires that you
	     know something about dot attributes.  Simple attributes are, e.g.,
	     "color=red".
	  */
	  public void setClassAttribute( Class cz, String attrib ) {
	    classAttributeMap.put( cz, attrib );
	  }

	  /**
	     Set the DOT attributes for a specific field. This allows you to
	     change the appearance of certain edges in the output, but requires
	     that you know something about dot attributes.  Simple attributes
	     are, e.g., "color=blue".
	  */
	  public void setFieldAttribute( Field field, String attrib ) {
	    fieldAttributeMap.put( field, attrib );
	  }
	  
	  /**
	     Set the DOT attributes for all fields with this name.
	  */
	  public void setFieldAttribute( String field, String attrib ) {
	    fieldAttributeMap.put( field, attrib );
	  }

	  /**
	     Pretend the given field does not exist.
	  */
	  public void ignoreField( Field field ) {
	    ignoreSet.add( field );
	  }

	  /**
	     Pretend that no fields with this name exist.
	  */
	  public void ignoreField( String field ) {
	    ignoreSet.add( field );
	  }
	  

	  /**
	     Pretend this class has no fields.  I.e., make the class opaque.
	  */
	  public void ignoreFields( Class cz ) {
	    Field[] fs = cz.getDeclaredFields( );
	    for( int i = 0; i < fs.length; i++ )
	      ignoreField( fs[i] );
	  }
	  
	  /**
	     Treat objects of this class as primitives; i.e., <code>toString</code>
	     is called on the object, and the result displayed in the label like
	     a primitive field.  
	  */
	  public void treatAsPrimitive( Class cz ) {
	    pretendPrimitiveSet.add( cz );
	  }
	  
	  /**
	     Treat objects from this package as primitives; i.e.,
	     <code>toString</code> is called on the object, and the result displayed
	     in the label like a primitive field.
	  */
	  public void treatAsPrimitive( Package pk ) {
	    pretendPrimitiveSet.add( pk );
	  }
	  
	  private Map classAttributeMap   = new HashMap( );
	  private Map fieldAttributeMap   = new HashMap( );
	  private Set pretendPrimitiveSet = new HashSet( );
	  private Set ignoreSet           = new HashSet( );


	  /**
	     Allow private, protected and package-access fields to be shown.
	     This is only possible if the security manager allows
	     <code>ReflectPermission("suppressAccessChecks")</code> permission.
	     This is usually the case when running from an application, but
	     perhaps not from an applet or servlet.
	  */
	  public boolean ignorePrivateFields = false;


	  /**
	     Toggle whether or not to include the field name in the label for an
	     object.  This is currently all-or-nothing.  TODO: allow this to be
	     set on a per-class basis.
	  */
	  public boolean showFieldNamesInLabels = true;

	  /**
	     Toggle whether to display the class name in the label for an
	     object (false, the default) or to use the result of calling
	     toString (true).
	   */
	  //public boolean useToStringAsClassName = false;

	  /**
	     Toggle whether to display qualified nested class names in the
	     label for an object from the same package as Graphviz (true) or
	     to display an abbreviated name (false, the default).
	   */
	  public boolean qualifyNestedClassNames = false;
	  public boolean showPackageNamesInClasses = true;

	  private boolean canTreatAsPrimitive( Object obj ) {
	    return obj == null || canTreatClassAsPrimitive( obj.getClass( ) );
	  }

	  private boolean canTreatClassAsPrimitive( Class cz ) {
	    if( cz == null || cz.isPrimitive( ) )
	      return true;

	    do {
	      if( pretendPrimitiveSet.contains( cz )
	       || pretendPrimitiveSet.contains( cz.getPackage( ) )
	          )
	        return true;

	      Class[] ifs = cz.getInterfaces( );
	      for( int i = 0; i < ifs.length; i++ )
	        if( canTreatClassAsPrimitive( ifs[i] ) )
	          return true;

	      cz = cz.getSuperclass( );
	    } while( cz != null );
	    return false;
	  }


	  private boolean looksLikePrimitiveArray( Object obj ) {
	    Class c = obj.getClass( );
	    if( canTreatClassAsPrimitive( c.getComponentType( ) ) )
	      return true;
	    int len = Array.getLength( obj );
	    for( int i = 0; i < len; i++ )
	      if( ! canTreatAsPrimitive( Array.get(obj, i) ) )
	        return false;
	    return true;
	  }


	  private boolean canIgnoreField( Field field ) {
	    return
	      Modifier.isStatic( field.getModifiers( ) )
	      || ignoreSet.contains( field )
	      || ignoreSet.contains( field.getName( ) )
	      ;
	  }
	  
	  private static final String canAppearUnquotedInLabelChars = " $&*@#!-+()^%;[],;.=";
	  private static boolean canAppearUnquotedInLabel( char c ) {
	    return canAppearUnquotedInLabelChars.indexOf( c ) != -1
	      || Character.isLetter( c )
	      || Character.isDigit( c )
	      ;
	  }

	  private static final String quotable = "\"<>{}|";

	  private static String quote( String s ) {
	    StringBuffer sb = new StringBuffer( );
	    for( int i = 0, n = s.length( ); i < n; i++ ) {
	      char c = s.charAt(i);
	      if( quotable.indexOf(c) != -1 )
	        sb.append( '\\' ).append( c );
	      else
	        if( canAppearUnquotedInLabel( c ) )
	          sb.append( c );
	        else
	          sb.append("\\\\0u" ).append( Integer.toHexString( (int)c ) );
	    }
	    return sb.toString( );
	  }
	  
	  private static boolean redefinesToString( Object obj ) {
		  Method[] ms = obj.getClass( ).getMethods( );
		  for( int i = 0; i < ms.length; i++ )
		    if( ms[i].getName( ).equals( "toString" ) && ms[i].getDeclaringClass( ) != Object.class )
		      return true;
		  return false;
		}

	  protected String className( Object obj, boolean useToStringAsClassName ) {
	    if( obj == null )
	      return "";

	    Class c = obj.getClass( );
	    if( useToStringAsClassName && redefinesToString( obj ) )
	      return quote(obj.toString( ));
	    else {
	      String name = c.getName( );
	      if( ! showPackageNamesInClasses || c.getPackage( ) == this.getClass( ).getPackage( ) ) {
	        //- Strip away everything before the last .
	        name = name.substring( name.lastIndexOf( '.' )+1 );
	        
	        if( ! qualifyNestedClassNames )
	          name = name.substring( name.lastIndexOf( '$' )+1 );
	      }
	      return name;
	    }
	  }
	}