import exception.StateNotFoundException;
import models.*;
import utils.BufferManager;
import utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class Laxer {

    private List<StateTransition> currentStateList = new ArrayList<>();
    private List<Token> generatedTokenList;

    public Laxer() {
        initialize();
    }

    private void initialize(){
        generatedTokenList  =new ArrayList<>();
    }

    public List<Token> getGeneratedTokenList() {
        return generatedTokenList;
    }

    public void getNextToken() {
        currentStateList.clear();
        currentStateList.add(new StateTransition("Q0", null));
        char lexeme = BufferManager.getInstance().getNextCharFromBuffer();
        StringBuilder generatedToken = new StringBuilder();
        List<StateTransition> exploredNewState = new ArrayList<>();
        while (!BufferManager.getInstance().isEOF()) {
            exploredNewState.clear();
            for (StateTransition currentState : currentStateList) {
//                System.out.println("Current State:" + currentState.getTransitionState());
                List<Lexeme> lexemeList = TokenManager.getInstance().getPossibleTokenListFromInput(lexeme);
                for (Lexeme token : lexemeList) {
                    try {
//                        System.out.println("Found Lexeme:" + token);
                        StateTransition transitionState = StateTransitionTable.getInstance().getStateTransition(currentState.getTransitionState(), token);
                        if (transitionState != null) {
                            exploredNewState.add(transitionState);
//                            System.out.println("Transition State:" + transitionState.getTransitionState());
                        }
                    } catch (StateNotFoundException e) {
                    }
                }
            }

            if (!exploredNewState.isEmpty()) {
                currentStateList.clear();
                currentStateList.addAll(exploredNewState);
                generatedToken.append(lexeme);
                lexeme = BufferManager.getInstance().getNextCharFromBuffer();
            } else {
                boolean isTokenFound = false;
                for (StateTransition currentState : currentStateList) {
                    State state = StateTransitionTable.getInstance().getStateInfo(currentState.getTransitionState());
                    if (state != null && state.isFinalState()) {
                        Token token = new Token(currentState.getTokenTypeGenerated(),generatedToken.toString(),BufferManager.getInstance().getCurrentLineNumber(),BufferManager.getInstance().getLexemeForward());
                        generatedTokenList.add(token);
                        /*System.out.println("Generated Token Type::" + currentState.getTokenTypeGenerated().getReservedTokenType());
                        System.out.println("Generated Token::" + generatedToken);*/
                        isTokenFound = true;
                    }
                }

                // TODO check for token not generated if invalid lexemes ...
                if (!isTokenFound) {
                    if (generatedToken.length() == 0) {
                        //TODO add whitespace issue ...
                        List<Lexeme> lexemeList = TokenManager.getInstance().getPossibleTokenListFromInput(lexeme);
                        if(!lexemeList.isEmpty()) {
                            generatedToken.append(lexeme);
                        }
                        lexeme = BufferManager.getInstance().getNextCharFromBuffer();
                    }
                    if(generatedToken.length() >0) {

                        TokenType tokenType = TokenManager.getInstance().getErrorTypeFromGeneratedErrorToken(generatedToken.toString(),currentStateList);
                        Token token = new Token(tokenType,generatedToken.toString(),BufferManager.getInstance().getCurrentLineNumber(),BufferManager.getInstance().getLexemeForward());
                        generatedTokenList.add(token);
                        /*System.out.println("Generated Token Type::" + TokenType.INVALID_NUMBER );
                        System.out.println("Generated Token::" + generatedToken);
                        System.out.println("CurrentStatus::" + currentStateList.get(0).getTransitionState());*/
                    }
                }
                generatedToken = new StringBuilder();

                currentStateList.clear();
                currentStateList.add(new StateTransition("Q0", null));
            }
        }
    }

}
