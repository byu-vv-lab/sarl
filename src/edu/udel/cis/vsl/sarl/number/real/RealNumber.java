package edu.udel.cis.vsl.sarl.number.real;

import edu.udel.cis.vsl.sarl.IF.NumberIF;

public abstract class RealNumber implements NumberIF {

	/**
	 * Order is: all real integers, followed by all real rationals, followed by
	 * everything else.
	 */
	@Override
	public int compareTo(NumberIF o) {
		if (this instanceof RealInteger) {
			if (o instanceof RealInteger) {
				return ((RealInteger) this).value().compareTo(
						((RealInteger) o).value());
			}
			return -1;
		} else if (this instanceof RealRational) {
			if (o instanceof RealRational) {
				RealRational me = (RealRational) this;
				RealRational you = (RealRational) o;
				int result = me.numerator().compareTo(you.numerator());

				if (result != 0)
					return result;
				result = me.denominator().compareTo(you.denominator());
				return result;
			} else if (o instanceof RealInteger)
				return 1;
			return -1;
		} else {
			throw new RuntimeException("Should not happen");
		}
	}
}
