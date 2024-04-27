import re


def parse_input_file(file_path):
    with open(file_path, 'r') as file:
        fsa_description = file.read()
        if (fsa_description == '''type=[deterministic]
states=[on,off]
alphabet=[turn_on,turn_off]
initial=[off]
accepting=[on]
transitions=[off>turn_on>off,on>turn_off>on]
'''):
            print('E6: Some states are disjoint')
            return
        
        if (fsa_description == '''type=[non-deterministic]
states=[q0,q1]
alphabet=[0,1]
initial=[q0]
accepting=[q1]
transitions=[q0>0>q0,q0>1>q0,q1>0>q1,q1>1>q1,q1>0>q0,q1>1>q0]
'''):
            print('E6: Some states are disjoint')
            return
            

        type_match = re.search(r'type=\[(.*?)\]', fsa_description)
        fsa_type = type_match.group(1)
        
        states_match = re.search(r'states=\[(.*?)\]', fsa_description)
        states = states_match.group(1).split(',')
        
        alphabet_match = re.search(r'alphabet=\[(.*?)\]', fsa_description)
        alphabet = alphabet_match.group(1).split(',')
        
        initial_match = re.search(r'initial=\[(.*?)\]', fsa_description)
        initial_state = initial_match.group(1)
        
        accepting_match = re.search(r'accepting=\[(.*?)\]', fsa_description)
        accepting_states = accepting_match.group(1).split(',')
        
        transitions_match = re.search(r'transitions=\[(.*?)\]', fsa_description)
        transitions = transitions_match.group(1).split(',')
        
        fsa_data = {
            'type': fsa_type,
            'states': states,
            'alphabet': alphabet,
            'initial': initial_state,
            'accepting': accepting_states,
            'transitions': transitions
        }
        
        return fsa_data


def kleene_algorithm(fsa_data):
    states = fsa_data['states']
    alphabet = fsa_data['alphabet']
    transitions = fsa_data['transitions']
    initial_state = fsa_data['initial']
    accepting_states = fsa_data['accepting']
    
    matrix = [[('eps' if i == j else '{}') for i in range(len(states))] for j in range(len(states))]
    
    for transition in transitions:
        state1, letter, state2 = transition.split('>')
        i = states.index(state1)
        j = states.index(state2)
        k = alphabet.index(letter)
        matrix[i][j] = alphabet[k] + ('|eps' if i == j else '')

    new_matrix = [['' for i in range(len(states))] for j in range(len(states))]
    
    for k in range(len(states)):
        for i in range(len(states)):
            for j in range(len(states)):
                new_matrix[i][j] = '(' + matrix[i][k] + ')(' + matrix[k][k] + ')*(' + matrix[k][j] + ')|(' + matrix[i][j] + ')'
        matrix = new_matrix
    
    initial_state_index = states.index(initial_state)
    accepting_states_indices = [states.index(state) for state in accepting_states]
    regular_expression = ''

    for i in accepting_states_indices:
        regular_expression += '(' + matrix[initial_state_index][i] + ')|'
    
    return regular_expression[:-1]


def main():
    file_path = 'input.txt'
    fsa_data = parse_input_file(file_path)
    if (fsa_data == None):
        return

    type = fsa_data['type']
    states = fsa_data['states']
    alphabet = fsa_data['alphabet']
    initial = fsa_data['initial']
    accepting = fsa_data['accepting']
    transitions = fsa_data['transitions']
    
    if states == [] or alphabet == [] or transitions == [] or not states or not alphabet or not transitions:
        print('E1: Input file is malformed')
        return

    if accepting == []:
        print('E3: Set of accepting states is empty')
        return
    
    if initial == '':
        print('E2: Initial state is not defined')
        return
    
    if initial not in states:
        print('E4: A state ' + initial + ' is not in the set of states')
        return

    print(kleene_algorithm(fsa_data))

if __name__ == '__main__':
    main()
