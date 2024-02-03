#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

#define NAME_SIZE 15        // The maximum size of name according to the condition
#define MAX_SIZE 1000       // The maximum size for both strings, read from file, and array of players

// Define enumeration Position that will be used in structure Player
enum Position {
    Goalkeeper, Defender, Midfielder, Forward
};

// Define structure Player and declare the array of Players
struct Player {
    int ID;                 // Player ID
    char name[NAME_SIZE];   // Player name
    enum Position position; // Player position
    int age;                // Player age
    int goalsAmount;        // Player goals amount
} players[MAX_SIZE];        // Array of players
int playersAmount = 0;      // Number of players in the array

// Prototype auxiliary functions
bool checkCommand(char *command);                   // Check if the given string is a valid command
bool ID_uniqueness(int ID);                         // Check uniqueness of ID
bool nameValidity(const char *name);                // Check Validity of name
void readString(char *string, FILE *input);         // Read the string from file and delete the last symbol '\n'
void copyPlayer(struct Player *dest, const struct Player source); // Copy the source Player to the destination Player
bool checkNumber(char *string);                     // Check if the given string is a valid number

// Prototype necessary methods for Player
bool addPlayer(struct Player *player, FILE *input, char *command, bool updateMode); // Add a new player to the players array
void deletePlayer(int ID, FILE *output);            // Delete a player from the players array
bool displayTeam(FILE *output);                     // Display the team of players
void searchPlayers(int ID, FILE *output);            // Search for a player in the players array
bool updatePlayer(FILE *input, char *command);      // Update a player in the players array

int main(void)
{
    FILE* input = fopen("input.txt", "r");  // Open input file
    FILE* output = fopen("output.txt", "w"); // Open output file

    char curCommand[MAX_SIZE], possibleCommand[MAX_SIZE] = "Nothing";
    while (!feof(input)) {
        if (!strcmp(possibleCommand, "Nothing")) readString(curCommand, input); // If the command has been already read, there is no need to read it again

// There are few ifs that check if string equals to command and execute the corresponding function
// Sometimes it is necessary to read extra data, such as Player ID

        if (!strcmp(curCommand, "Add")) {
            strcpy(curCommand, "");
            if(!addPlayer(NULL, input, possibleCommand, false)) {
                fputs("Invalid inputs\n", output);
                return 0;
            }

            if (strcmp(possibleCommand, "Nothing")) {
                strcpy(curCommand, possibleCommand);
                strcpy(possibleCommand, "Nothing");
                continue;
            }
        }
        else if (!strcmp(curCommand, "Delete")) {
            strcpy(curCommand, "");
            char string[MAX_SIZE];
            readString(string, input);

            if (!checkNumber(string)) {
                printf("Invalid inputs\n");
                return false;
            }
            int ID = atoi(string);

            if (checkCommand(string)) {
                strcpy(curCommand, string);
                strcpy(possibleCommand, "Nothing");
                continue;
            }

            deletePlayer(ID, output);
        }
        else if (!strcmp(curCommand, "Display")) {
            strcpy(curCommand, "");
            if (!displayTeam(output)) return 0;
        }
        else if (!strcmp(curCommand, "Search")) {
            strcpy(curCommand, "");
            char string[MAX_SIZE];
            readString(string, input);

            if (!checkNumber(string)) {
                printf("Invalid inputs\n");
                return false;
            }
            int ID = atoi(string);

            if (checkCommand(string)) {
                strcpy(curCommand, "");
                strcpy(curCommand, string);
                strcpy(possibleCommand, "Nothing");
                continue;
            }

            searchPlayers(ID, output);
        }
        else if (!strcmp(curCommand, "Update")) {
            strcpy(curCommand, "");
            if (!updatePlayer(input, possibleCommand)) {
                fputs("Invalid inputs\n", output);
                return 0;
            }

            if (strcmp(possibleCommand, "Nothing")) {
                strcpy(curCommand, possibleCommand);
                strcpy(possibleCommand, "Nothing");
                continue;
            }
        }
        else if (strcmp(curCommand, "")) {
            strcpy(curCommand, "");
            fputs("Invalid inputs\n", output);
            return 0;
        }
    }

    fclose(input);  // Close input file
    fclose(output); // Close output file
    return 0;
}

// Check uniqueness of ID
bool ID_uniqueness(int ID)
{
    for (int i = 0; i < playersAmount; i++)
        if (ID == players[i].ID) return false;
    return true;
}

// Check Validity of name
bool nameValidity(const char *name)
{
    int size = strlen(name);
    if (name[0] < 65 || name[0] > 90) return false;
    if (size < 2 || size > 15) return false;
    for (int i = 0; i < size; i++)
        if (!(name[i] >= 65 && name[i] <= 90 || name[i] >= 97 && name[i] <= 122)) return false;
    return true;
}

// Read the string from file and delete the last symbol '\n'
void readString(char *string, FILE *input)
{
    fgets(string, MAX_SIZE - 1, input);
    string[strlen(string) - 1] = '\0';
}

// Check if the given string is a valid command
bool checkCommand(char *command)
{
    if (strcmp(command, "Add") && strcmp(command, "Delete") && strcmp(command, "Update") && strcmp(command, "Search") && strcmp(command, "Display")) return false;
    return true;
}

// Copy the source Player to the destination Player
void copyPlayer(struct Player *dest, const struct Player source)
{
    dest->ID = source.ID;
    strcpy(dest->name, source.name);
    dest->position = source.position;
    dest->age = source.age;
    dest->goalsAmount = source.goalsAmount;
}

// Check if the given string is a valid number
bool checkNumber(char *string)
{
    for (int i = 0; i < strlen(string); i++)
        if ((int) string[i] < 48 || (int) string[i] > 57) return false;
    return true;
}

/**
 * Reads a string from input and checks if it is a valid command or player name/position/age/goals.
 * If it is a valid command, stores it in the command variable and returns true.
 * If it is a valid player name/position/age/goals, stores it in the tempPlayer variable and returns true.
 * Otherwise, returns false.
 * @param string The string to be checked.
 * @param input The input file stream.
 * @param command The command variable to be updated if the string is a valid command.
 * @param tempPlayer The temporary player variable to be updated if the string is a valid player name/position/age/goals.
 * @param updateMode A boolean indicating whether the function is being called in update mode.
 * @return true if the string is a valid command or player name/position/age/goals, false otherwise.
 * Also every time I check if the input string equals to any possible command.
*/
bool addPlayer(struct Player *player, FILE *input, char *command, bool updateMode)
{
    char string[MAX_SIZE];
    struct Player tempPlayer;

    readString(string, input);
    if (checkCommand(string)) {
        strcpy(command, string);
        return true;
    }
    tempPlayer.ID = atoi(string);
    if (!checkNumber(string) || ID_uniqueness(tempPlayer.ID) == updateMode || !tempPlayer.ID && strcmp(string, "0")) return false;

    readString(string, input);
    if (checkCommand(string)) {
        strcpy(command, string);
        return true;
    }
    if (!nameValidity(string)) return false;
    strcpy(tempPlayer.name, string);

    readString(string, input);
    if (checkCommand(string)) {
        strcpy(command, string);
        return true;
    }
    if (!strcmp(string, "Goalkeeper")) tempPlayer.position = Goalkeeper;
    else if (!strcmp(string, "Defender")) tempPlayer.position = Defender;
    else if (!strcmp(string, "Midfielder")) tempPlayer.position = Midfielder;
    else if (!strcmp(string, "Forward")) tempPlayer.position = Forward;
    else return false;

    readString(string, input);
    if (checkCommand(string)) {
        strcpy(command, string);
        return true;
    }
    tempPlayer.age = atoi(string);
    if (!checkNumber(string) || tempPlayer.age < 18 || tempPlayer.age > 100) return false;

    readString(string, input);
    if (checkCommand(string)) {
        strcpy(command, string);
        return true;
    }
    tempPlayer.goalsAmount = atoi(string);
    if (!checkNumber(string) || tempPlayer.goalsAmount < 0 || tempPlayer.goalsAmount >= 1000) return false;

    if (!updateMode) copyPlayer(&players[playersAmount++], tempPlayer);
    else copyPlayer(player, tempPlayer);
    return true;
}

/**
 * Deletes the player with the given ID from the players array.
 * If the player is not found, prints an error message to the output file stream.
 * @param ID The ID of the player to be deleted.
 * @param output The output file stream.
*/
void deletePlayer(int ID, FILE *output)
{
    for (int i = 0; i < playersAmount; i++)
        if (players[i].ID == ID) {
            for (int j = i; j < playersAmount - 1; j++) players[j] = players[j + 1];
            --playersAmount;
            return;
        }
    fputs("Impossible to delete\n", output);
}

/**
 * Displays the players in the players array to the output file stream.
 * If the players array is empty, prints an error message to the output file stream.
 * @param output The output file stream.
 * @return true if the players array is not empty, false otherwise.
*/
bool displayTeam(FILE *output)
{
    if (!playersAmount) {
        fputs("Invalid inputs\n", output);
        return false;
    }

    for (int i = 0; i < playersAmount; i++) {
        fprintf(output, "ID: %d, Name: %s, Position: ", players[i].ID, players[i].name);
        if (players[i].position == Goalkeeper) fputs("Goalkeeper", output);
        if (players[i].position == Defender) fputs("Defender", output);
        if (players[i].position == Midfielder) fputs("Midfielder", output);
        if (players[i].position == Forward) fputs("Forward", output);
        fprintf(output, ", Age: %d, Goals: %d\n", players[i].age, players[i].goalsAmount);
    }

    return true;
}

/**
 * Searches for the player with the given ID in the players array.
 * If the player is found, prints "Found" to the output file stream.
 * Otherwise, prints "Not found" to the output file stream.
 * @param ID The ID of the player to be searched for.
 * @param output The output file stream.
*/
void searchPlayers(int ID, FILE *output)
{
    for (int i = 0; i < playersAmount; i++)
        if (players[i].ID == ID) {
            fputs("Found\n", output);
            return;
        }
    fputs("Not found\n", output);
}

/**
 * Updates the player with the given ID in the players array.
 * If the command is not "Nothing", updates the player with the given ID in the players array with the new player data.
 * @param input The input file stream.
 * @param command The command variable.
 * @return true if the player is updated, false otherwise.
*/
bool updatePlayer(FILE *input, char *command)
{
    struct Player player;
    if (!addPlayer(&player, input, command, true)) return false;
    if (strcmp(command, "Nothing")) return true;

    for (int i = 0; i < playersAmount; i++)
        if (players[i].ID == player.ID) {
            copyPlayer(&players[i], player);
            return true;
        }
    return true;
}
