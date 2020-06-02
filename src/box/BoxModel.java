/**
 */
package box;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link box.BoxModel#getBoxes <em>Boxes</em>}</li>
 * </ul>
 *
 * @see box.BoxPackage#getBoxModel()
 * @model
 * @generated
 */
public interface BoxModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Boxes</b></em>' containment reference list.
	 * The list contents are of type {@link box.Box}.
	 * It is bidirectional and its opposite is '{@link box.Box#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Boxes</em>' containment reference list.
	 * @see box.BoxPackage#getBoxModel_Boxes()
	 * @see box.Box#getModel
	 * @model opposite="Model" containment="true"
	 * @generated
	 */
	EList<Box> getBoxes();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String toString();

} // BoxModel
