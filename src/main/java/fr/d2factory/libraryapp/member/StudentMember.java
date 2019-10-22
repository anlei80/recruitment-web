package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.WalletInsufficientException;

public class StudentMember extends Member {

	private boolean firstYear;
	
	@Override
	public void payBook(int numberOfDays) throws WalletInsufficientException {
		
		int shouldPayedDays = 0;
		float amount = 0f;
		//If student is first year
		if (firstYear) {
			shouldPayedDays = numberOfDays - 15;
		} else {
			shouldPayedDays = numberOfDays;
		}
		
		
		if (shouldPayedDays > 0 && shouldPayedDays > getLimitDays()) {
			amount = 0.1f * getLimitDays() + 0.15f * (shouldPayedDays - getLimitDays());
		} else if (shouldPayedDays > 0 && shouldPayedDays <= getLimitDays()) {
			amount = 0.1f * shouldPayedDays;
		}
		
		if (getWallet() >= amount) {
			setWallet(getWallet() - amount);
		} else {
			throw new WalletInsufficientException("You should charge your wallet");
		}

	}

	@Override
	public int getLimitDays() {
		// TODO Auto-generated method stub
		return 30;
	}

	public boolean isFirstYear() {
		return firstYear;
	}

	public void setFirstYear(boolean firstYear) {
		this.firstYear = firstYear;
	}
	
	

}
