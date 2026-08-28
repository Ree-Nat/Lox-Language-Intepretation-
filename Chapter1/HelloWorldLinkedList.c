#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
 
typedef struct Node{
    struct Node *prev;
    struct Node *next;
    char *data;
} Node;


char* allocateString(char *string_input);
Node* createNode(char *data);
void insert(Node** head, char *data);
Node* find(Node* head, char *data);
void delete(Node** head, char* data);

Node* createNode(char *data)
{
    Node* newNode = (Node*)malloc(sizeof(Node));
    char* data_string = allocateString(data);
    newNode->data = data_string;
    newNode->next = NULL;
    newNode->prev = NULL;
    return newNode;
}

char* allocateString(char *string_input)
{
    size_t length = strlen(string_input) + 1;
    char* heap_str = (char*)malloc(length*sizeof(char));
    strcpy(heap_str, string_input);
    return heap_str;

}


void insert(Node** head, char *data)
{
    Node* newNode = createNode(data);
    printf("created node with string: %s\n", data);
    if(*head == NULL)
    {
        *head = newNode;
        return;
    }

    Node* temp = *head;
    while (temp->next != NULL) {
        temp = temp->next;
    }
    temp->next = newNode;
    newNode->prev = temp;
}

void delete(Node** head, char* data){
    Node* foundNode = find(*head, data); 
    if(foundNode == NULL)
    {
        printf("cannot delete node, node is not found");
        return;
    }
    Node* nextNode = foundNode->next;
    Node* prevNode = foundNode->prev;

    if (prevNode != NULL)
    {
        prevNode->next = nextNode;

    } else{
        *head = nextNode;
    }

    if(nextNode != NULL)
    {
        nextNode->prev = prevNode;
    }
    
    printf("\n%s is deleted", foundNode->data);
    //removes heap allocated string 
    free(foundNode->data);
    free(foundNode);
}

Node* find(Node* head, char *data) {
    Node* current = head;
    int count = 0;
    while(current != NULL)
    {
        if(strcmp(current->data, data) == 0)
        {
            printf("\n%s is found at pos %d", data, count);
            return current;
        }
        current = current->next;
        count += 1;
    }
    printf("node not found");
    return NULL;
}

void listNodes(Node* head) {
    Node* current = head;
    printf("\nListing nodes");
    while(current != NULL)
    {
        printf("\n%s", current->data);
        current = current->next;
    }
}


int main()
{
    printf("Hello world\n");

    Node* head = NULL;

    insert(&head, "meow");
    insert(&head, "woof"); 
    insert(&head, "chirp"); 
    insert(&head, "bark"); 
    find(head, "meow");
    find(head, "bark");
    delete(&head, "chirp");
    listNodes(head);
    return 0; 
} 

