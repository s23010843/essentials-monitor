fetch('app/src/main/res/values/strings.xml')
    .then(response => response.text())
    .then(str => {
        const parser = new DOMParser();
        const xml = parser.parseFromString(str, "application/xml");

        // Get all <string> elements
        const stringElements = xml.querySelectorAll('string');

        stringElements.forEach(strElem => {
            const name = strElem.getAttribute('name');
            const value = strElem.textContent;

            // Find all elements with class equal to string name and update textContent
            document.querySelectorAll(`.${name}`).forEach(el => {
                el.textContent = value;
            });
        });
    })
    .catch(err => {
        console.error('Error loading strings.xml:', err);
    });