function showTab(tab) {
    hideAllTabs();
    tab.style.display = 'block';
}

function hideAllTabs() {
    let tabs = document.getElementsByClassName('tab');
    Array.prototype.forEach.call(tabs, (tab) => {
        tab.style.display = 'none';
    });
}