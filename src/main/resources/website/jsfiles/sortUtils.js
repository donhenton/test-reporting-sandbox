
function sampleSort(sortedInput)
{
    var obj = JSON3.parse(sortedInput);
    var sortedObj = {};
    sortedObj['results'] = [];
    sortedObj.results = obj.results.sort(function (a, b)
    {

        return a.id.localeCompare(b.id);

    });

    return JSON3.stringify(sortedObj);
}