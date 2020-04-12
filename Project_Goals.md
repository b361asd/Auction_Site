# Project Goals
 
1. Create accounts of users; login, logout
2. Auctions
	* Seller creates auctions and posts items for sale
		* Set all the characteristics of the item
		* Set closing date and time
		* Set a hidden minimum price (reserve)
	* A buyer should be able to bid
		* Manually
			* Let the buyer set a new bid
		* With automatic bidding
			* Set a secret upper limit
			* Put in a higher bid automatically for the user in case someone bids higher
	* Define the winner of the auction
		* When the closing time has come, check if the seller has set a reserve
			* If yes: if the reserve is higher than the last bid, no one is the winner.
			* If no: whoever has the higher bid is the winner

3. Browsing and advanced search functionality
	* Let people browse on the items and see the status of the current bidding
	* Sort by different criteria (by type, bidding price, etc.)
	* Search the list of items by various criteria.
	* A user should be able to:
		* View all the history of bids for any specific auction
		* View the list of all auctions a specific buyer or seller has participated in
		* View the list of "similar" items on auctions in the preceding month (and auction information about them)

4. Alerts and messaging functions
	* Alert the buyer that a higher bid has been placed
	* Alert the buyer in case someone bids more than your upper limit (for automatic bidding)
	* Let user set an alert for specific items s/he is interested
		* Get an alert when the item becomes available
	* User can post questions
	* User can search and browse questions/answers

5. Customer representatives & admin functions
	* Admin (create an admin account ahead of time)
		* Create accounts for customer representatives
		* Generates sales reports for:
			* Total earnings
			* Earnings per:
				* Item
				* Item type
				* End-user
			* Best-selling items
			* Best buyers
	* Customer representative:
		* Answers to questions of users
		* Edits account information, bids and auctions
		* Remove bids
		* Removes auctions
