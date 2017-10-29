package com.app.framework.listeners;

import com.app.framework.utilities.map.model.TurnByTurnModel;

import java.util.List;

/**
 * Created by leonard on 5/4/2017.
 */

public interface TurnByTurnListener {

    /**
     * Interface for when Google Directions API has successfully provided turn by turn directions
     *
     * @param turnByTurnList List of turn by turn directions
     */
    void onSuccess(List<TurnByTurnModel> turnByTurnList);

    /**
     * Interface for when Google turn by turn directions fails
     */
    void onFailure();
}
