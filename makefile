CC=gcc
CFLAGS=-I.

main: main.o myBank.o
	$(CC) -o main main.o myBank.o
	
main.o: main.c
	$(CC) -c $(FLAGS) main.c

myBank.o: myBank.c myBank.h
	$(CC) -c $(FLAGS) myBank.c 
	
		
