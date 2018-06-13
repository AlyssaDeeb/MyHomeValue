export const setPropDetails = (
    {
        id = '',
        hometype = '',
        yearbuilt = '',
        bedrooms= '---',
        bathrooms= '',
        squarefootage= '',
        ppsf= '',
        acreage= '',
        parking= '',
        stories= '',
        value= '--',
        addressLine= '',
        city= '',
        state= '',
        zip= '',
        price= '',
        saleDate= '',
        growthPercent= '',
        ele_school= '',
        mid_school= '',
        high_school= '',
        ele_school_rank= '',
        mid_school_rank= '',
        high_school_rank= '',
        walkScore= '',
        transitScore= '',
        walkScore_desc= '',
        url='',
        lat= 0,
        long= 0,
        comparables= [],
        homeSaved = false,
        loading = true
    } = {}
) => ({
     type: 'SET_PROPDETAILS',

        id,
        hometype,
        yearbuilt,
        bedrooms,
        bathrooms,
        squarefootage,
        ppsf,
        acreage,
        parking,
        stories,
        value,
        addressLine,
        city,
        state,
        zip,
        price,
        saleDate,
        growthPercent,
        ele_school,
        mid_school,
        high_school,
        ele_school_rank,
        mid_school_rank,
        high_school_rank,
        walkScore,
        transitScore,
        walkScore_desc,
        url,
        lat,
        long,
        comparables,
        homeSaved,
        loading
});

export const setSavedHome = ({homeSaved = false} = {}) => ({
     type: 'SET_SAVEDHOME',
    homeSaved

});

export const setLoading = ({loading = true} = {}) => ({
    type: 'SET_LOADING',
    loading

});

export const setCookieID = ({cookieID = 0} = {}) => ({
     type: 'SET_COOKIEID',
    cookieID

});
