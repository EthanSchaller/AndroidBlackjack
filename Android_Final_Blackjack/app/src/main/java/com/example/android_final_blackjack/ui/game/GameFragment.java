package com.example.android_final_blackjack.ui.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_final_blackjack.R;
import com.example.android_final_blackjack.databinding.FragmentGameBinding;

import java.util.ArrayList;
import java.util.Collections;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;

    //setting up variables to be used later
    ImageView plyrCrd1, plyrCrd2, plyrCrd3, plyrCrd4, plyrCrd5,
              housCrd1, housCrd2, housCrd3, housCrd4, housCrd5;
    TextView houseScore, playerScore, playerName, totalPoints;
    Button bttnHit, bttnStand, bttnStart;
    SharedPreferences shrPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //collecting data from the UI
        playerScore = root.findViewById(R.id.plyr_Ttl);
        houseScore = root.findViewById(R.id.hous_Ttl);
        playerName = root.findViewById(R.id.plyr_Name);
        totalPoints = root.findViewById(R.id.TtlPnts);

        bttnHit = root.findViewById(R.id.bttn_Hit);
        bttnStand = root.findViewById(R.id.bttn_Stnd);
        bttnStart = root.findViewById(R.id.bttn_Strt);

        plyrCrd1 = root.findViewById(R.id.plr_crd1);
        plyrCrd2 = root.findViewById(R.id.plr_crd2);
        plyrCrd3 = root.findViewById(R.id.plr_crd3);
        plyrCrd4 = root.findViewById(R.id.plr_crd4);
        plyrCrd5 = root.findViewById(R.id.plr_crd5);

        housCrd1 = root.findViewById(R.id.hous_crd1);
        housCrd2 = root.findViewById(R.id.hous_crd2);
        housCrd3 = root.findViewById(R.id.hous_crd3);
        housCrd4 = root.findViewById(R.id.hous_crd4);
        housCrd5 = root.findViewById(R.id.hous_crd5);

        //connecting to the shared preference
        shrPref = getActivity().getSharedPreferences("AppShrPrefs", Context.MODE_PRIVATE);

        //making a user variable
        player user;

        //setting the user's name to the one saved in the shared preference
        if(shrPref.getString("name", "Player").isEmpty()) {
            user = new player();
        } else {
            user = new player(shrPref.getString("name", "Player"));
        }


        //setting the user's points to the one saved in the shared preference
        user.setPoints(shrPref.getInt("points", 0));

        //setting the deck and and making a new variable to keep the sorted cards
        ArrayList<card> deck = new ArrayList<>(0);
        ArrayList<card> srtDeck = deckCreator(deck);

        //setting the house's and user's deck to the unsorted deck
        player house = new player();
        house.setHand(generateHand(srtDeck, 'h'));
        user.setHand(generateHand(srtDeck, 'c'));

        //setting the labels in the view
        playerName.setText(user.getName() + "'s Deck");
        houseScore.setText("Total Score:  " + house.getScore());
        playerScore.setText("Total Score:  " + user.getScore());
        totalPoints.setText("Total Points: " + user.getPoints());

        //setting an on click listener
        bttnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changing the visibility of the buttons
                bttnHit.setVisibility(View.VISIBLE);
                bttnStand.setVisibility(View.VISIBLE);
                bttnStart.setVisibility(View.INVISIBLE);

                //shuffling the deck
                ArrayList<card> rndDeck = Shuffle(srtDeck);

                //setting the house's hand to cards from the random deck
                house.setHand(generateHand(rndDeck, 'h'));
                for (int i = 0; i <= 4; i++) {
                    house.getHand().get(i).setVisible(0);
                }
                house.setScore(0);

                //setting the user's hand to cards from the random deck
                user.setHand(generateHand(rndDeck, 'u'));
                for (int i = 2; i <= 4; i++) {
                    user.getHand().get(i).setVisible(0);
                }
                user.setScore(addScore(user.getHand()));

                //if your starting cards equal 21 you instantly win
                if (user.getScore() == 21) {
                    user.setPoints(user.getPoints() + 50);
                    totalPoints.setText("Total Points: " + user.getPoints());

                    bttnHit.setVisibility(View.INVISIBLE);
                    bttnStand.setVisibility(View.INVISIBLE);
                    bttnStart.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), user.getName() + " WINS!!!\n    (that's you)", Toast.LENGTH_SHORT).show();
                }

                //changing the visibility of all the card images
                chngCrdPic(user.getHand().get(0), plyrCrd1);
                chngCrdPic(user.getHand().get(1), plyrCrd2);
                chngCrdPic(new card(), plyrCrd3);
                chngCrdPic(new card(), plyrCrd4);
                chngCrdPic(new card(), plyrCrd5);

                chngCrdPic(new card(), housCrd1);
                chngCrdPic(new card(), housCrd2);
                chngCrdPic(new card(), housCrd3);
                chngCrdPic(new card(), housCrd4);
                chngCrdPic(new card(), housCrd5);

                //setting the score text on screen
                houseScore.setText("Total Score:  " + house.getScore());
                playerScore.setText("Total Score:  " + user.getScore());
            }
        });

        //setting an on click listener to the hit button
        bttnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the hand has less than five cards then a normal hit is done
                if (CrdVisCont(user.getHand()) < 5) {
                    user.setHand(hit(user.getHand()));
                    switch (CrdVisCont(user.getHand())) {
                        case 1:
                            chngCrdPic(user.getHand().get(0), plyrCrd1);
                            break;
                        case 2:
                            chngCrdPic(user.getHand().get(1), plyrCrd2);
                            break;
                        case 3:
                            chngCrdPic(user.getHand().get(2), plyrCrd3);
                            break;
                        case 4:
                            chngCrdPic(user.getHand().get(3), plyrCrd4);
                            break;
                        case 5:
                            chngCrdPic(user.getHand().get(4), plyrCrd5);
                            break;
                    }

                    user.setScore(addScore(user.getHand()));

                    playerScore.setText("Total Score:  " + user.getScore());
                }

                //if the user's score is 21 then they instantly win
                if (user.getScore() == 21) {
                    user.setPoints(user.getPoints() + 50);
                    totalPoints.setText("Total Points: " + user.getPoints());

                    SharedPreferences.Editor prefEdit = shrPref.edit();
                    prefEdit.putInt("points", user.getPoints());
                    prefEdit.commit();

                    bttnHit.setVisibility(View.INVISIBLE);
                    bttnStand.setVisibility(View.INVISIBLE);
                    bttnStart.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), user.getName() + " WINS!!!\n    (that's you)", Toast.LENGTH_SHORT).show();

                //if the user's score is bigger than 21 the user busts and instantly looses
                } else if (user.getScore() > 21) {
                    user.setPoints(user.getPoints() - 50);
                    totalPoints.setText("Total Points: " + user.getPoints());

                    SharedPreferences.Editor prefEdit = shrPref.edit();
                    prefEdit.putInt("points", user.getPoints());
                    prefEdit.commit();

                    bttnHit.setVisibility(View.INVISIBLE);
                    bttnStand.setVisibility(View.INVISIBLE);
                    bttnStart.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "THE HOUSE WINS!!!\n     it always does...", Toast.LENGTH_SHORT).show();

                //if the amount of cards drawn is 5 the users turn is over
                } else if (CrdVisCont(user.getHand()) == 5) {
                    bttnHit.setVisibility(View.INVISIBLE);
                    bttnStand.setVisibility(View.INVISIBLE);
                    bttnStart.setVisibility(View.VISIBLE);

                    houseTurn(user, house);

                    SharedPreferences.Editor prefEdit = shrPref.edit();
                    prefEdit.putInt("points", user.getPoints());
                    prefEdit.commit();
                }
            }
        });

        //setting an on click listener to the stand button
        bttnStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bttnHit.setVisibility(View.INVISIBLE);
                bttnStand.setVisibility(View.INVISIBLE);
                bttnStart.setVisibility(View.VISIBLE);

                houseTurn(user, house);

                SharedPreferences.Editor prefEdit = shrPref.edit();
                prefEdit.putInt("points", user.getPoints());
                prefEdit.commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    //making the card class
    class card {
        protected String Suit;
        protected String CrdType;
        protected int CrdVlu;
        protected int visible;

        //setting up setters and getters
        public String getSuit() {
            return this.Suit;
        }
        public String getCrdType() {
            return this.CrdType;
        }
        public int getCrdVlu() {
            return this.CrdVlu;
        }
        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        //setting up constructors
        public card() {
            Suit = "";
            CrdType = "Back";
            CrdVlu = 0;
            visible = 0;
        }

        public card(String suit, String crdName, int crdVlu) {
            Suit = suit;
            CrdType = crdName;
            CrdVlu = crdVlu;
            visible = 0;
        }

    }

    //creating the player class
    class player {
        protected String Name;
        protected int Points;
        protected int Score;
        protected ArrayList<card> Hand;
        protected int numCrdDrwn;

        //setting up the constructors
        public player() {
            Name = "Player";
            Points = 0;
            Hand = new ArrayList<>();
            numCrdDrwn = 0;
        }

        public player(String s) {
            Name = s;
            Points = 0;
            Hand = new ArrayList<>();
            numCrdDrwn = 0;
        }

        //setting up setters and getteds
        public String getName() {
            return this.Name;
        }
        public int getPoints() {
            return this.Points;
        }
        public int getScore() {
            return this.Score;
        }
        public ArrayList<card> getHand() {
            return this.Hand;
        }

        public void setName(String name) {
            this.Name = name;
        }
        public void setPoints(int points) {
            this.Points = points;
        }
        public void setScore(int score) {
            this.Score = score;
        }
        public void setHand(ArrayList<card> hand) {
            this.Hand = hand;
        }
    }

    //function to add cards to the deck
    public ArrayList<card> addCard(ArrayList<card> deck, String s1, String s2, int i1) {
        card temp = new card(s1, s2, i1);
        deck.add(temp);
        return deck;
    }

    //function to add the numbers on the cards
    public ArrayList<card> addCardName(ArrayList<card> deck, String s) {
        deck = addCard(deck, s, "2", 2);
        deck = addCard(deck, s, "3", 3);
        deck = addCard(deck, s, "4", 4);
        deck = addCard(deck, s, "5", 5);
        deck = addCard(deck, s, "6", 6);
        deck = addCard(deck, s, "7", 7);
        deck = addCard(deck, s, "8", 8);
        deck = addCard(deck, s, "9", 9);
        deck = addCard(deck, s, "10", 10);
        deck = addCard(deck, s, "j", 10);
        deck = addCard(deck, s, "q", 10);
        deck = addCard(deck, s, "k", 10);
        deck = addCard(deck, s, "a", 11);

        return deck;
    }

    //creating the detect that has peen
    public ArrayList<card> deckCreator(ArrayList<card> deck) {
        deck = addCardName(deck, "spade");
        deck = addCardName(deck, "club");
        deck = addCardName(deck, "diamond");
        deck = addCardName(deck, "heart");

        return deck;
    }

    //shuffling the deck
    public ArrayList<card> Shuffle(ArrayList<card> deck) {
        Collections.shuffle(deck);
        return deck;
    }

    //generating a function to create the hand
    public ArrayList<card> generateHand(ArrayList<card> deck, char c) {
        int i = 0;
        if (c == 'h') {
            i = 5;
        }

        ArrayList<card> Hand = new ArrayList<>();

        Hand.add(deck.get(i));
        Hand.get(0).setVisible(1);
        Hand.add(deck.get(i + 1));
        Hand.get(1).setVisible(1);
        Hand.add(deck.get(i + 3));
        Hand.add(deck.get(i + 4));
        Hand.add(deck.get(i + 5));

        return Hand;
    }

    //changing to the house's tern
    public void houseTurn(player user, player house) {
        int i = 0;
        for (i = 1; i <= 2; i++) {
            house.setHand(hit(house.getHand()));
            switch (i) {
                case 1:
                    chngCrdPic(house.getHand().get(0), housCrd1);
                    break;
                case 2:
                    chngCrdPic(house.getHand().get(1), housCrd2);
                    break;
            }

            house.setScore(addScore(house.getHand()));
        }

        while (house.getScore() < user.getScore() && i <= 5) {
            house.setHand(hit(house.getHand()));
            switch (i) {
                case 3:
                    chngCrdPic(house.getHand().get(2), housCrd3);
                    break;
                case 4:
                    chngCrdPic(house.getHand().get(3), housCrd4);
                    break;
                case 5:
                    chngCrdPic(house.getHand().get(4), housCrd5);
                    break;
            }

            house.setScore(addScore(house.getHand()));

            i++;
        }

        houseScore.setText("Total Score:  " + house.getScore());

        if(user.getScore()>house.getScore()||house.getScore()>21) {
            user.setPoints(user.getPoints() + 50);
            totalPoints.setText("Total Points: " + user.getPoints());
            Toast.makeText(getActivity(), user.getName() + " WINS!!!\n    (that's you)", Toast.LENGTH_SHORT).show();
        } else if(house.getScore()>=user.getScore()&&house.getScore()<=21) {
            user.setPoints(user.getPoints() - 50);
            totalPoints.setText("Total Points: " + user.getPoints());
            Toast.makeText(getActivity(), "THE HOUSE WINS!!!\n     it always does...", Toast.LENGTH_SHORT).show();
        }
    }


    //counter the amount of cards drawn
    public int CrdVisCont(ArrayList<card> hand) {
        int temp = 0;
        for(int i = 0; i <= 4; i++) {
            if(hand.get(i).getVisible() == 1) {
                temp++;
            }
        }
        return temp;
    }

    //setting up a hit function
    public ArrayList<card> hit(ArrayList<card> hand) {
        hand.get(CrdVisCont(hand)).setVisible(1);

        return hand;
    }

    //this function adds a number to the score tytpe
    public int addScore(ArrayList<card> hand) {
        int Score = 0;
        for(int i = 0; i <= 4; i++) {
            if(hand.get(i).getVisible() == 1) {
                if(hand.get(i).getCrdVlu() == 11 && Score + 11 > 21) {
                    Score += 1;
                } else {
                    Score += hand.get(i).getCrdVlu();
                }
            }

        }

        return Score;
    }

    //setting up a function to be called upon and change the image of any of the cards to the appropriate one
    //--==There is an error with the images for the face cards in the program itself where it is saying that their is no such things.
    // This is weird becuase it has no problem running for me and, even after a few restarts of the app and phone vm, doesn't change
    // whether it works with these errors showing ==--
    public void chngCrdPic(@NonNull card card, ImageView img) {
        String temp = card.getSuit() + " " + card.getCrdType();
        switch(temp) {
            case "club 2": img.setBackgroundResource(R.drawable.card_clubs_02);
                break;
            case "club 3": img.setBackgroundResource(R.drawable.card_clubs_03);
                break;
            case "club 4": img.setBackgroundResource(R.drawable.card_clubs_04);
                break;
            case "club 5": img.setBackgroundResource(R.drawable.card_clubs_05);
                break;
            case "club 6": img.setBackgroundResource(R.drawable.card_clubs_06);
                break;
            case "club 7": img.setBackgroundResource(R.drawable.card_clubs_07);
                break;
            case "club 8": img.setBackgroundResource(R.drawable.card_clubs_08);
                break;
            case "club 9": img.setBackgroundResource(R.drawable.card_clubs_09);
                break;
            case "club 10": img.setBackgroundResource(R.drawable.card_clubs_10);
                break;
            case "club j": img.setBackgroundResource(R.drawable.card_clubs_j);
                break;
            case "club q": img.setBackgroundResource(R.drawable.card_clubs_q);
                break;
            case "club k": img.setBackgroundResource(R.drawable.card_clubs_k);
                break;
            case "club a": img.setBackgroundResource(R.drawable.card_clubs_a);
                break;

            case "diamond 2": img.setBackgroundResource(R.drawable.card_diamonds_02);
                break;
            case "diamond 3": img.setBackgroundResource(R.drawable.card_diamonds_03);
                break;
            case "diamond 4": img.setBackgroundResource(R.drawable.card_diamonds_04);
                break;
            case "diamond 5": img.setBackgroundResource(R.drawable.card_diamonds_05);
                break;
            case "diamond 6": img.setBackgroundResource(R.drawable.card_diamonds_06);
                break;
            case "diamond 7": img.setBackgroundResource(R.drawable.card_diamonds_07);
                break;
            case "diamond 8": img.setBackgroundResource(R.drawable.card_diamonds_08);
                break;
            case "diamond 9": img.setBackgroundResource(R.drawable.card_diamonds_09);
                break;
            case "diamond 10": img.setBackgroundResource(R.drawable.card_diamonds_10);
                break;
            case "diamond j": img.setBackgroundResource(R.drawable.card_diamonds_j);
                break;
            case "diamond q": img.setBackgroundResource(R.drawable.card_diamonds_q);
                break;
            case "diamond k": img.setBackgroundResource(R.drawable.card_diamonds_k);
                break;
            case "diamond a": img.setBackgroundResource(R.drawable.card_diamonds_a);
                break;

            case "spade 2": img.setBackgroundResource(R.drawable.card_spades_02);
                break;
            case "spade 3": img.setBackgroundResource(R.drawable.card_spades_03);
                break;
            case "spade 4": img.setBackgroundResource(R.drawable.card_spades_04);
                break;
            case "spade 5": img.setBackgroundResource(R.drawable.card_spades_05);
                break;
            case "spade 6": img.setBackgroundResource(R.drawable.card_spades_06);
                break;
            case "spade 7": img.setBackgroundResource(R.drawable.card_spades_07);
                break;
            case "spade 8": img.setBackgroundResource(R.drawable.card_spades_08);
                break;
            case "spade 9": img.setBackgroundResource(R.drawable.card_spades_09);
                break;
            case "spade 10": img.setBackgroundResource(R.drawable.card_spades_10);
                break;
            case "spade j": img.setBackgroundResource(R.drawable.card_spades_j);
                break;
            case "spade q": img.setBackgroundResource(R.drawable.card_spades_q);
                break;
            case "spade k": img.setBackgroundResource(R.drawable.card_spades_k);
                break;
            case "spade a": img.setBackgroundResource(R.drawable.card_spades_a);
                break;

            case "heart 2": img.setBackgroundResource(R.drawable.card_hearts_02);
                break;
            case "heart 3": img.setBackgroundResource(R.drawable.card_hearts_03);
                break;
            case "heart 4": img.setBackgroundResource(R.drawable.card_hearts_04);
                break;
            case "heart 5": img.setBackgroundResource(R.drawable.card_hearts_05);
                break;
            case "heart 6": img.setBackgroundResource(R.drawable.card_hearts_06);
                break;
            case "heart 7": img.setBackgroundResource(R.drawable.card_hearts_07);
                break;
            case "heart 8": img.setBackgroundResource(R.drawable.card_hearts_08);
                break;
            case "heart 9": img.setBackgroundResource(R.drawable.card_hearts_09);
                break;
            case "heart 10": img.setBackgroundResource(R.drawable.card_hearts_10);
                break;
            case "heart j": img.setBackgroundResource(R.drawable.card_hearts_j);
                break;
            case "heart q": img.setBackgroundResource(R.drawable.card_hearts_q);
                break;
            case "heart k": img.setBackgroundResource(R.drawable.card_hearts_k);
                break;
            case "heart a": img.setBackgroundResource(R.drawable.card_hearts_a);
                break;

            case " Back": img.setBackgroundResource(R.drawable.card_back);
        }
    }
}