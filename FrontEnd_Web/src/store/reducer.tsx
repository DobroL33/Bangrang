import { AppState } from "./state";

const initialState: AppState = {
  // isPage: 1,
};

const reducer = (
  state: AppState = initialState,
  action: { type: string; payload: any }
) => {
  switch (action.type) {
    // case "SET_PAGE":
    //   return { ...state, isPage: action.payload };
    default:
      return state;
  }
};
export default reducer;
