#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#define MAX 50
int numOfAccounts = 0;
double accounts[MAX][2];

int main()
{

    char whatFunction = '1';
    // printf("Enter a function:");
    //whatFunction = getchar();
    int *p_numOfAccounts;
    int bol = 1;

    double(*p_accounts)[MAX][2] = &accounts;

    p_numOfAccounts = &numOfAccounts;

    while (bol==1)
    {
        printf("Please choose a transaction type:\nO-Open Account\nB-Balance Inquiry\nD-Deposit\nW-Withdrawal\nC-Close Account\nI-Interest\nP-Print\nE-Exit\n");

        scanf(" %c", &whatFunction);
        // whatFunction = getchar();
        switch (whatFunction)
        {
        case 'O':
            O(p_accounts, p_numOfAccounts);
            fflush(stdin);
            break;
        case 'B':
            B(p_accounts);
            fflush(stdin);
            break;
        case 'D':
            D(p_accounts);
            fflush(stdin);
            break;
        case 'W':
            W(p_accounts);
            fflush(stdin);
            break;
        case 'C':
            C(p_accounts, p_numOfAccounts);
            fflush(stdin);
            break;
        case 'I':
            I(p_accounts);
            fflush(stdin);
            break;
        case 'E':
            bol=0;
            break;
        case 'P':
            P(p_accounts, p_numOfAccounts);
            fflush(stdin);
            break;

        default:
            printf("Invalid transaction type \n");
            break;
        }
        FlushStdin();
        
    }

    return 0;
}
