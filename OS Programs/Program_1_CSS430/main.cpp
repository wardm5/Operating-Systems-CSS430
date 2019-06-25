#include <stdio.h>
#include <iostream>
#include <unistd.h>    // h file for fork, pipe
#include <stdlib.h>    // h file for exit
#include <sys/wait.h>  // h file for wait
#include <string.h>

using namespace std;

    /************************************************************************
     *                                                                       *
     *   Code by: Misha Ward                                                 *
     *   CSS 430: Program 1 - Practicing with Pipes and Processes            *
     *   This code is ment to replicate the command line input of:           *
     *      $ ps -A | grep argv[1] | wc -l                                   *
     *   Code utilizes pipes to transfer information from one process to     *
     *   another.                                                            *
     *   NOTE: code was written in Atom, formatting might not transfer.      *
     *   NOTE: Answers are off due to extra process of ./a.out, check        *
     *         png screenshots to see the extra process from terminal        *
     *                                                                       *
     ************************************************************************/

int main(int arg, const char ** args) {
    pid_t pid;  // creates process variable
    int filedes1[2];  // file destination 1
    int filedes2[2];  // file destination 2
    const int read = 0;  // sets read to 0
    const int write = 1; // sets write to 1
    if (arg < 2) {  // if arguments are less than 2, provide user error report
        perror("Error: not enough arguments.");
        return 0;
    }
    if (pipe(filedes1) < 0) {  // create first pipe or provide user error report.
        perror("ERROR: pipe not working.");
    }
    if (pipe(filedes2) < 0) {  // create second pipe or provide user error report.
        perror("ERROR: pipe not working.");
    }
    if ((pid = fork()) < 0) {  // create the first child process
        perror("Error: Failed to Fork");
        return EXIT_FAILURE;
    } else if (pid == 0) {  // if process ID equals 0...
        if ((pid = fork()) < 0) {  // create the grandchild process
            perror("Error");
            return EXIT_FAILURE;
        }
        else if (pid == 0) { // if process ID equals 0...
            if ((pid = fork()) < 0) { // create the great-grandchild process
                perror("Error");
                return EXIT_FAILURE;
            } else if (pid == 0) {  // if process id equals 0...
                close(filedes2[read]);      // the great grandchild closes the reading for pipe 2
                close(filedes2[write]);     // the great grandchild closes the writing for pipe 2
                close(filedes1[read]);        // the great grandchild closes the reading for pipe 1
                dup2(filedes1[write], write);     // the great grandchild sets up the writing for pipe 1
                execlp("ps", "ps", "-A", NULL);  // changes the process to execute new "ps" process.
            } else {
                close(filedes1[write]);            // the grandchild closes the writing for pipe 1
                dup2(filedes1[read], read);        // the grandchild sets up the reading for pipe 1
                close(filedes2[read]);            // the grandchild closes the reading for pipe 2
                dup2(filedes2[write], write);    // the grandchild sets up the writing for pipe 2
                execlp("grep", "grep", args[1], NULL);  // changes the process to execute new "grep" process
            }
        } else {
            close(filedes1[write]);  // the child closes the writing for pipe 1
            close(filedes1[read]);   // the child closes the reading for pipe 1
            close(filedes2[write]);  // the child closes the writing for pipe 1
            dup2(filedes2[read], read);  // the Child sets up the reading for pipe 2
            execlp("wc", "wc", "-l", NULL);  // changes the process to execute new "wc" process
        }
    }
    else {
        close(filedes1[read]);  // closes pipe 1 for reading
        close(filedes1[write]); // closes pipe 1 for writing
        close(filedes2[read]);  // closes pipe 2 for reading
        close(filedes2[write]); // closes pipe 2 for writing
        wait(NULL);  // waits for all child processes to complete.
    }
    //    system("ps -A | grep kworker");    // tests
    //    system("ps -A | grep kworker | wc -l");  // tests
    return 0; // program finishes successfully.
}
