const propDetailsReducerDefaultState = {
    id: '',
    hometype: '',
    yearbuilt: '',
    bedrooms: '---',
    bathrooms: '',
    squarefootage: '',
    ppsf: '',
    acreage: '',
    parking: '',
    stories: '',
    value: '--',
    addressLine: '',
    city: '',
    state: '',
    zip: '',
    price: '',
    saleDate: '',
    growthPercent: '',
    ele_school: '',
    mid_school: '',
    high_school: '',
    ele_school_rank: '',
    mid_school_rank: '',
    high_school_rank: '',
    walkScore: '',
    transitScore: '',
    walkScore_desc: '',
    url: '',
    lat: 0,
    long: 0,
    comparables: [],
    homeSaved: false,
    cookieID: 0
};
export default (state = propDetailsReducerDefaultState, action) => {
    switch (action.type) {
        case 'SET_PROPDETAILS':
            return {
                ...state,
                id: action.id,
                hometype: action.hometype,
                yearbuilt: action.yearbuilt,
                bedrooms: action.bedrooms,
                bathrooms: action.bathrooms,
                squarefootage: action.squarefootage,
                ppsf: action.ppsf,
                acreage: action.acreage,
                parking: action.parking,
                stories: action.stories,
                value: action.value,
                addressLine: action.addressLine,
                city: action.city,
                state: action.state,
                zip: action.zip,
                price: action.price,
                saleDate: action.saleDate,
                growthPercent: action.growthPercent,
                ele_school: action.ele_school,
                mid_school: action.mid_school,
                high_school: action.high_school,
                ele_school_rank: action.ele_school_rank,
                mid_school_rank: action.mid_school_rank,
                high_school_rank: action.high_school_rank,
                walkScore: action.walkScore,
                transitScore: action.transitScore,
                walkScore_desc: action.walkScore_desc,
                url: action.url,
                lat: action.lat,
                long: action.long,
                comparables: action.comparables,
                homeSaved: action.homeSaved

            };

        case 'SET_COOKIEID':
            return {
                ...state,
                cookieID: action.cookieID
            }
        case 'SET_SAVEDHOME':
            return {
                ...state,
                homeSaved: action.homeSaved
            }
            case 'SET_LOADING':
            return {
                ...state,
                loading: action.loading
            }
        default:
            return state;
    }
};
