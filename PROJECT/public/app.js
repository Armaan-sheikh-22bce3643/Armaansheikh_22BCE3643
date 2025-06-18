document.getElementById('reportForm').addEventListener('submit', async (e) => {
    e.preventDefault();
  
    const form = e.target;
    const data = {
      type: form.type.value,
      itemName: form.itemName.value,
      description: form.description.value,
      location: form.location.value,
      contactInfo: form.contactInfo.value,
    };
  
    try {
      const res = await fetch('/api/items', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
  
      const result = await res.json();
  
      document.getElementById('message').innerText = 'Item reported successfully!';
      form.reset();
    } catch (err) {
      document.getElementById('message').innerText = 'Error reporting item.';
      console.error(err);
    }
  });
  