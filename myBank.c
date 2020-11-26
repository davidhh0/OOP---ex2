#include "myBank.h"
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

void FlushStdin(void) {
  int ch;
  while(((ch = getchar()) !='\n') && (ch != EOF));
}

void O(double (*_accounts)[50][2], int *_numOfAccounts)
{

    if (*_numOfAccounts < 50)
    {
        int whereTo = 0;
        for (int i = 0; i < 50; i++)
        {
            if ((*_accounts)[i][0] == 0)
            {
                whereTo = i;
                break;
            }
        }

        printf("Please enter amount for deposit: ");
        double toDeposit;
        int check = scanf(" %lf", &toDeposit);
        if (toDeposit < 0 || check != 1)
        {
            printf("Failed to read the amount \n");
            fflush(stdin);
        }
        else
        {
            (*_accounts)[whereTo][0] = 1;
            (*_accounts)[whereTo][1] = toDeposit;
            printf("New account to the bank: %d \n", (whereTo + 901));
            ++*_numOfAccounts;
        }
    }
    else
    {
        printf("The bank is full");
    }
}

void B(double (*_accounts)[50][2])
{
    printf("Enter the account number: ");
    int accountNumber;
    int check = scanf(" %d", &accountNumber);
    if (check != 1)
    { // the account is NOT a number
        printf("Failed to read the account number\n");
        fflush(stdin);
    }
    else if ((*_accounts)[accountNumber - 901][0] == 0)
    {
        //the account is NOT active
        printf("This account is closed \n");
        fflush(stdin);
    }

    else
    {
        double balance = (*_accounts)[accountNumber - 901][1];
        printf("The balace of account number %d is: %.2lf \n", accountNumber, balance);
    }
}

void D(double (*_accounts)[50][2])
{
    printf("Enter the account number: ");
    int accountNumber;
    double amountToDeposit;
    int checkAccountNumber = scanf(" %d", &accountNumber);
    if (checkAccountNumber != 1)
    {
        printf("Failed to read the account number\n");
        fflush(stdin);
    }
    else if ((*_accounts)[accountNumber - 901][0] == 0)
    {
        // the account it NOT active
        printf("This account is closed \n");
        fflush(stdin);
    }

    else
    {
        printf("Enter amount to deposit: ");
        int checkamoutToDepist = scanf(" %lf", &amountToDeposit);
        if (checkamoutToDepist != 1)
        {
            printf("Failed to read the amount number\n");
            fflush(stdin);
        }
        else if (amountToDeposit < 0)
        {
            printf("You can't deposit negative value! \n");
            return;
        }
        else
        {
            double balance = (*_accounts)[accountNumber - 901][1];
            (*_accounts)[accountNumber - 901][1] = balance + amountToDeposit;
            printf("New balance: %.2lf \n", (balance + amountToDeposit));
        }
    }
}

void W(double (*_accounts)[50][2])
{
    printf("Enter the account number: ");
    int accountNumber;
    double amountToWithdrawal;
    int checkAccountNumber = scanf(" %d", &accountNumber);
    if (checkAccountNumber!=1){
        printf("Failed to read the account number\n");
        fflush(stdin);

    }
    else if ((*_accounts)[accountNumber - 901][0] == 0){
        // the account it NOT active
        printf("There is not an account: %d \n", accountNumber);
        fflush(stdin);
    }
    else if ((*_accounts)[accountNumber - 901][0] == 1)
    {
        printf("Enter amount to withdrawal: ");
        int checkAmountToWithdrawal = scanf(" %lf", &amountToWithdrawal);
        if (checkAmountToWithdrawal!=1){
            printf("Failed to read the amount number\n");
            fflush(stdin);
        }
        else if (amountToWithdrawal < 0)
        {
            printf("You can't withdrawal negative value! \n");
            return;
        }
        else if ((*_accounts)[accountNumber - 901][1] < amountToWithdrawal)
        {
            printf("Cannot withdraw more than the balance \n");
        }
        else
        {
            double balance = (*_accounts)[accountNumber - 901][1];
            (*_accounts)[accountNumber - 901][1] = balance - amountToWithdrawal;
            printf("New balance: %.2lf \n", (balance - amountToWithdrawal));
        }
    }

}

void C(double (*_accounts)[50][2], int *_numOfAccounts)
{
    printf("Enter the account number: ");
    int accountNumber;
    int checkAccountNumber = scanf(" %d", &accountNumber);
    if(checkAccountNumber!=1){
        printf("Failed to read the account number\n");
        fflush(stdin);
    }
    else if ((*_accounts)[accountNumber - 901][0] == 0)
    {
        printf("This account is closed \n");
    }
    else
    {
        (*_accounts)[accountNumber - 901][0] = 0;
        printf("Account: %d closed.\n", accountNumber);
        --*_numOfAccounts;
    }
}

void I(double (*_accounts)[50][2])
{
    printf("Please enter interest rate: ");
    double percent;
    int checkPercent = scanf(" %lf", &percent);
    if(checkPercent!=1 || percent<=0){
        printf("Failed to read the interest rate\n");
        fflush(stdin);
    }
    else{
    for (int i = 0; i < 50; i++)
    {
        double balance = (*_accounts)[i][1];
        (*_accounts)[i][1] = balance + (percent / 100) * balance;
    }
    }
}

void P(double (*_accounts)[50][2], int *_numOfAccounts)
{
    int i = 0;
    if (*_numOfAccounts == 0)
    {
        printf("No accounts in the bank \n");
    }
    else
    {
        for (i = 0; i < 50; i++)
        {
            if ((*_accounts)[i][0] == 1)
            {
                double balance = (*_accounts)[i][1];
                printf("Account number: %d , balance: %.2lf \n", (i + 901), balance);
            }
        }
    }
}

