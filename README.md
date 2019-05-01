# MoneyTreeLite
Moneytree Light displays accounts and transactions for Japanese users. 
<br/> All of the data is currently included in the bundle as JSON files, I have placed these files in "Assets"
<br/>
<br/> There are two screens. The first is a list of accounts and the total balance of all the accounts.
The top of the screen shows the total amount. The rest displays a list of accounts. The list
of accounts is grouped by institution name. Each row in the list shows the name of the
account and the balance and should be ordered based on the account's name.
<br/>
<br/>Tapping on an account brings up the second screen. This screen is a list of transactions for that
account. The top of the screen displays account details, including transaction balance. 
The list of transactions is grouped by the month, and each row displays
the date, description and amount.
<br/>
<br/>

# Account Structure
The source code is documented for logical explanations.
![alt text](https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/accounts_structure.jpg)

# Transaction structure
The source code is documented for logical explanations.
The structure is similar to Accounts Structure.

# Dependency Injection
![alt text](https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/dependency_structure.jpg)

# Screen Shots
![alt text](https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/accounts.png)
![alt text](https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/transactions.png)

# Covered User Stories
1. In the account list, a user should be able to see a list of accounts and the total balance
of those accounts in the user's currency. The list should be ordered by the account
name and grouped by institution. Each row should display the name and the
balance in that account's currency. <br/>
2. In the account list, a user should be able to tap on the account to see the list of
transactions on that account. The transactions should be ordered by month, from the
newest to the oldest. Each row in the list should display the transaction’s date,
description and amount in the account's currency. <br/>
3. In the transaction list, a user should see the transactions grouped by month, with each
header displaying the month, the year, and the cash flow (money in and money out)
during that month. <br/>
4. In the transaction list, a user should be able to delete a transaction from the list after
confirming that they really want to delete the transaction.

#Link error
https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/lint-results.html

# Code Coverage
https://github.com/SupriyaNaveen/MoneyTreeLite/blob/master/readmeImages/CodeCoverage.PNG

# Accessibility
I have worked on Talkback when working with HCL Technologies. Need to label view with focusable true.

# Road map
I would have tried to achieve the calculations with currency. Right now Only one currency considered for calculations.<br/>
Handling huge amount of data with pagination.<br/>
Tool bar UI components for header. <br/>
Code coverage.
