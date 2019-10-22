package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.WalletInsufficientException;

public class ResidentMember extends Member {
	
	@Override
	public void payBook(int numberOfDays) throws WalletInsufficientException {
		
		float amount = 0f;
		
		if (numberOfDays > getLimitDays()) {
			amount = 0.1f * getLimitDays() + 0.2f * (numberOfDays - getLimitDays());
		} else if (numberOfDays <= getLimitDays()) {
			amount = 0.1f * numberOfDays;
		}
		
		if (getWallet() >= amount) {
			setWallet(getWallet() - amount);
		} else {
			throw new WalletInsufficientException("You should charge your wallet");
		}
	}

	@Override
	public int getLimitDays() {
		return 60;
	}
}
