package ua.coolboy.particlemodels.utils;

import java.io.Serializable;

import org.bukkit.util.Vector;

/**
 * This class implements rotations in a three-dimensional space.
 *
 * <p>
 * Rotations can be represented by several different mathematical entities
 * (matrices, axe and angle, Cardan or Euler angles, quaternions). This class
 * presents an higher level abstraction, more user-oriented and hiding this
 * implementation details. Well, for the curious, we use quaternions for the
 * internal representation. The user can build a rotation from any of these
 * representations, and any of these representations can be retrieved from a
 * <code>Rotation</code> instance (see the various constructors and getters). In
 * addition, a rotation can also be built implicitly from a set of vectors and
 * their image.</p>
 * <p>
 * This implies that this class can be used to convert from one representation
 * to another one. For example, converting a rotation matrix into a set of
 * Cardan angles from can be done using the following single line of code:</p>
 * <pre>
 * double[] angles = new Rotation(matrix, 1.0e-10).getAngles(RotationOrder.XYZ);
 * </pre>
 * <p>
 * Focus is oriented on what a rotation <em>do</em> rather than on its
 * underlying representation. Once it has been built, and regardless of its
 * internal representation, a rotation is an <em>operator</em> which basically
 * transforms three dimensional {@link Vector3D vectors} into other three
 * dimensional {@link Vector3D vectors}. Depending on the application, the
 * meaning of these vectors may vary and the semantics of the rotation also.</p>
 * <p>
 * For example in an spacecraft attitude simulation tool, users will often
 * consider the vectors are fixed (say the Earth direction for example) and the
 * frames change. The rotation transforms the coordinates of the vector in
 * inertial frame into the coordinates of the same vector in satellite frame. In
 * this case, the rotation implicitly defines the relation between the two
 * frames.</p>
 * <p>
 * Another example could be a telescope control application, where the rotation
 * would transform the sighting direction at rest into the desired observing
 * direction when the telescope is pointed towards an object of interest. In
 * this case the rotation transforms the direction at rest in a topocentric
 * frame into the sighting direction in the same topocentric frame. This implies
 * in this case the frame is fixed and the vector moves.</p>
 * <p>
 * In many case, both approaches will be combined. In our telescope example, we
 * will probably also need to transform the observing direction in the
 * topocentric frame into the observing direction in inertial frame taking into
 * account the observatory location and the Earth rotation, which would
 * essentially be an application of the first approach.</p>
 *
 * <p>
 * These examples show that a rotation is what the user wants it to be. This
 * class does not push the user towards one specific definition and hence does
 * not provide methods like <code>projectVectorIntoDestinationFrame</code> or
 * <code>computeTransformedDirection</code>. It provides simpler and more
 * generic methods: {@link #applyTo(Vector3D) applyTo(Vector3D)} and {@link
 * #applyInverseTo(Vector3D) applyInverseTo(Vector3D)}.</p>
 *
 * <p>
 * Since a rotation is basically a vectorial operator, several rotations can be
 * composed together and the composite operation <code>r = r<sub>1</sub> o
 * r<sub>2</sub></code> (which means that for each vector <code>u</code>,
 * <code>r(u) = r<sub>1</sub>(r<sub>2</sub>(u))</code>) is also a rotation.
 * Hence we can consider that in addition to vectors, a rotation can be applied
 * to other rotations as well (or to itself). With our previous notations, we
 * would say we can apply <code>r<sub>1</sub></code> to
 * <code>r<sub>2</sub></code> and the result we get is <code>r = r<sub>1</sub> o
 * r<sub>2</sub></code>. For this purpose, the class provides the methods:
 * {@link #applyTo(Rotation) applyTo(Rotation)} and
 * {@link #applyInverseTo(Rotation) applyInverseTo(Rotation)}.</p>
 *
 * <p>
 * Rotations are guaranteed to be immutable objects.</p>
 *
 * @see Vector3D
 * @see RotationOrder
 * @since 1.2
 */

//Copied class from apache math3, works with Bukkit Vector, all right belongs to their authors
public class Rotation implements Serializable {

    /**
     * Identity rotation.
     */
    public static final Rotation IDENTITY = new Rotation(1.0, 0.0, 0.0, 0.0, false);

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -2153622329907944313L;

    /**
     * Scalar coordinate of the quaternion.
     */
    private final double q0;

    /**
     * First coordinate of the vectorial part of the quaternion.
     */
    private final double q1;

    /**
     * Second coordinate of the vectorial part of the quaternion.
     */
    private final double q2;

    /**
     * Third coordinate of the vectorial part of the quaternion.
     */
    private final double q3;

    /**
     * Build a rotation from the quaternion coordinates.
     * <p>
     * A rotation can be built from a <em>normalized</em> quaternion, i.e. a
     * quaternion for which q<sub>0</sub><sup>2</sup> +
     * q<sub>1</sub><sup>2</sup> + q<sub>2</sub><sup>2</sup> +
     * q<sub>3</sub><sup>2</sup> = 1. If the quaternion is not normalized, the
     * constructor can normalize it in a preprocessing step.</p>
     * <p>
     * Note that some conventions put the scalar part of the quaternion as the
     * 4<sup>th</sup> component and the vector part as the first three
     * components. This is <em>not</em> our convention. We put the scalar part
     * as the first component.</p>
     *
     * @param q0 scalar part of the quaternion
     * @param q1 first coordinate of the vectorial part of the quaternion
     * @param q2 second coordinate of the vectorial part of the quaternion
     * @param q3 third coordinate of the vectorial part of the quaternion
     * @param needsNormalization if true, the coordinates are considered not to
     * be normalized, a normalization preprocessing step is performed before
     * using them
     */
    public Rotation(double q0, double q1, double q2, double q3,
            boolean needsNormalization) {

        if (needsNormalization) {
            // normalization preprocessing
            double inv = 1.0 / Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
            q0 *= inv;
            q1 *= inv;
            q2 *= inv;
            q3 *= inv;
        }

        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;

    }

    /**
     * Build a rotation from an axis and an angle.
     *
     * @param axis axis around which to rotate
     * @param angle rotation angle
     * @since 3.6
     */
    public Rotation(final Vector axis, final double angle) {

        double norm = axis.length();
        if (norm == 0) {
            throw new RuntimeException("Vector can't be 0");
        }

        double halfAngle = -0.5 * angle;
        double coeff = Math.sin(halfAngle) / norm;

        q0 = Math.cos(halfAngle);
        q1 = coeff * axis.getX();
        q2 = coeff * axis.getY();
        q3 = coeff * axis.getZ();

    }

    /**
     * Build one of the rotations that transform one vector into another one.
     *
     * <p>
     * Except for a possible scale factor, if the instance were applied to the
     * vector u it will produce the vector v. There is an infinite number of
     * such rotations, this constructor choose the one with the smallest
     * associated angle (i.e. the one whose axis is orthogonal to the (u, v)
     * plane). If u and v are collinear, an arbitrary rotation axis is
     * chosen.</p>
     *
     * @param u origin vector
     * @param v desired image of u by the rotation
     */
    public Rotation(Vector u, Vector v) {

        double normProduct = u.length() * v.length();
        if (normProduct == 0) {
            throw new RuntimeException("Vector can't be 0");
        }

        double dot = u.dot(v);

        if (dot < ((2.0e-15 - 1.0) * normProduct)) {
            // special case u = -v: we select a PI angle rotation around
            // an arbitrary vector orthogonal to u
            Vector w = VertexUtil.orthogonal(u);
            q0 = 0.0;
            q1 = -w.getX();
            q2 = -w.getY();
            q3 = -w.getZ();
        } else {
            // general case: (u, v) defines a plane, we select
            // the shortest possible rotation: axis orthogonal to this plane
            q0 = Math.sqrt(0.5 * (1.0 + dot / normProduct));
            double coeff = 1.0 / (2.0 * q0 * normProduct);
            Vector q = v.crossProduct(u);
            q1 = coeff * q.getX();
            q2 = coeff * q.getY();
            q3 = coeff * q.getZ();
        }

    }

    /**
     * Build a rotation from three Cardan or Euler elementary rotations.
     *
     * <p>
     * Cardan rotations are three successive rotations around the canonical axes
     * X, Y and Z, each axis being used once. There are 6 such sets of rotations
     * (XYZ, XZY, YXZ, YZX, ZXY and ZYX). Euler rotations are three successive
     * rotations around the canonical axes X, Y and Z, the first and last
     * rotations being around the same axis. There are 6 such sets of rotations
     * (XYX, XZX, YXY, YZY, ZXZ and ZYZ), the most popular one being ZXZ.</p>
     * <p>
     * Beware that many people routinely use the term Euler angles even for what
     * really are Cardan angles (this confusion is especially widespread in the
     * aerospace business where Roll, Pitch and Yaw angles are often wrongly
     * tagged as Euler angles).</p>
     *
     * @param alpha1 angle of the first elementary rotation
     * @param alpha2 angle of the second elementary rotation
     * @param alpha3 angle of the third elementary rotation
     * @since 3.6
     */
    public Rotation(double alpha1, double alpha2, double alpha3) {
        Rotation r1 = new Rotation(new Vector(1, 0, 0), alpha1);
        Rotation r2 = new Rotation(new Vector(0, 1, 0), alpha2);
        Rotation r3 = new Rotation(new Vector(0, 0, 1), alpha3);
        Rotation composed = r1.compose(r2.compose(r3));
        q0 = composed.q0;
        q1 = composed.q1;
        q2 = composed.q2;
        q3 = composed.q3;
    }

    public Vector applyTo(Vector u) {

        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();

        double s = q1 * x + q2 * y + q3 * z;

        return new Vector(2 * (q0 * (x * q0 - (q2 * z - q3 * y)) + s * q1) - x,
                2 * (q0 * (y * q0 - (q3 * x - q1 * z)) + s * q2) - y,
                2 * (q0 * (z * q0 - (q1 * y - q2 * x)) + s * q3) - z);

    }

    /**
     * Apply the rotation to a vector stored in an array.
     *
     * @param in an array with three items which stores vector to rotate
     * @param out an array with three items to put result to (it can be the same
     * array as in)
     */
    public void applyTo(final double[] in, final double[] out) {

        final double x = in[0];
        final double y = in[1];
        final double z = in[2];

        final double s = q1 * x + q2 * y + q3 * z;

        out[0] = 2 * (q0 * (x * q0 - (q2 * z - q3 * y)) + s * q1) - x;
        out[1] = 2 * (q0 * (y * q0 - (q3 * x - q1 * z)) + s * q2) - y;
        out[2] = 2 * (q0 * (z * q0 - (q1 * y - q2 * x)) + s * q3) - z;

    }

    /**
     * Apply the inverse of the rotation to a vector.
     *
     * @param u vector to apply the inverse of the rotation to
     * @return a new vector which such that u is its image by the rotation
     */
    public Vector applyInverseTo(Vector u) {

        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();

        double s = q1 * x + q2 * y + q3 * z;
        double m0 = -q0;

        return new Vector(2 * (m0 * (x * m0 - (q2 * z - q3 * y)) + s * q1) - x,
                2 * (m0 * (y * m0 - (q3 * x - q1 * z)) + s * q2) - y,
                2 * (m0 * (z * m0 - (q1 * y - q2 * x)) + s * q3) - z);

    }

    /**
     * Apply the inverse of the rotation to a vector stored in an array.
     *
     * @param in an array with three items which stores vector to rotate
     * @param out an array with three items to put result to (it can be the same
     * array as in)
     */
    public void applyInverseTo(final double[] in, final double[] out) {

        final double x = in[0];
        final double y = in[1];
        final double z = in[2];

        final double s = q1 * x + q2 * y + q3 * z;
        final double m0 = -q0;

        out[0] = 2 * (m0 * (x * m0 - (q2 * z - q3 * y)) + s * q1) - x;
        out[1] = 2 * (m0 * (y * m0 - (q3 * x - q1 * z)) + s * q2) - y;
        out[2] = 2 * (m0 * (z * m0 - (q1 * y - q2 * x)) + s * q3) - z;

    }

    /**
     * Apply the instance to another rotation.
     * <p>
     * Calling this method is equivalent to call      {@link #compose(Rotation, RotationConvention)
   * compose(r, RotationConvention.VECTOR_OPERATOR)}.
     * </p>
     *
     * @param r rotation to apply the rotation to
     * @return a new rotation which is the composition of r by the instance
     */
    public Rotation applyTo(Rotation r) {
        return compose(r);
    }

    /**
     * Compose the instance with another rotation.
     * <p>
     * If the semantics of the rotations composition corresponds to a
     * {@link RotationConvention#VECTOR_OPERATOR vector operator} convention,
     * applying the instance to a rotation is computing the composition in an
     * order compliant with the following rule : let {@code u} be any vector and
     * {@code v} its image by {@code r1} (i.e. {@code r1.applyTo(u) = v}). Let
     * {@code w} be the image of {@code v} by rotation {@code r2} (i.e.
     * {@code r2.applyTo(v) = w}). Then {@code w = comp.applyTo(u)}, where
     * {@code comp = r2.compose(r1, RotationConvention.VECTOR_OPERATOR)}.
     * </p>
     * <p>
     * If the semantics of the rotations composition corresponds to a
     * {@link RotationConvention#FRAME_TRANSFORM frame transform} convention,
     * the application order will be reversed. So keeping the exact same meaning
     * of all {@code r1}, {@code r2}, {@code u}, {@code v}, {@code w} and
     * {@code comp} as above, {@code comp} could also be computed as
     * {@code comp = r1.compose(r2, RotationConvention.FRAME_TRANSFORM)}.
     * </p>
     *
     * @param r rotation to apply the rotation to
     * @param convention convention to use for the semantics of the angle
     * @return a new rotation which is the composition of r by the instance
     */
    public Rotation compose(final Rotation r) {
        return composeInternal(r);
    }

    /**
     * Compose the instance with another rotation using vector operator
     * convention.
     *
     * @param r rotation to apply the rotation to
     * @return a new rotation which is the composition of r by the instance
     * using vector operator convention
     */
    private Rotation composeInternal(final Rotation r) {
        return new Rotation(r.q0 * q0 - (r.q1 * q1 + r.q2 * q2 + r.q3 * q3),
                r.q1 * q0 + r.q0 * q1 + (r.q2 * q3 - r.q3 * q2),
                r.q2 * q0 + r.q0 * q2 + (r.q3 * q1 - r.q1 * q3),
                r.q3 * q0 + r.q0 * q3 + (r.q1 * q2 - r.q2 * q1),
                false);
    }
}
