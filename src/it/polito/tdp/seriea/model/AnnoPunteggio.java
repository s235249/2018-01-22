package it.polito.tdp.seriea.model;

public class AnnoPunteggio implements Comparable<AnnoPunteggio>{
	
	private Season s;
	private int punteggio;
	
	public AnnoPunteggio(Season s, int punteggio) {
		super();
		this.s = s;
		this.punteggio = punteggio;
	}
	public Season getS() {
		return s;
	}
	public void setS(Season s) {
		this.s = s;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + punteggio;
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnoPunteggio other = (AnnoPunteggio) obj;
		if (punteggio != other.punteggio)
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AnnoPunteggio [s=" + s + ", punteggio=" + punteggio + "]";
	}
	
	@Override
	public int compareTo(AnnoPunteggio o) {
		
		if (this.punteggio>o.getPunteggio())
			return 1;
		
			return -1;
	}
	
	

}
