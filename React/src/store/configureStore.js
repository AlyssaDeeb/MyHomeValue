import {createStore, combineReducers} from 'redux';
import propDetailsdReducer from '../reducers/propDetailsReducer';

export default () => {
    const store = createStore(
        combineReducers({
            propDetails: propDetailsdReducer      
        })
    );
    
    return store;
};
