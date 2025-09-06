from django.db import models
import uuid
# Create your models here.
class User(models.Model):
    userID = models.UUIDField(default = uuid.uuid4, editable = False, primary_key = True)
    username = models.CharField(max_length=15)
    password = models.CharField(max_length=50)
    rate = models.FloatField(default = 5)
    #ratetimes = models.IntegerField(default = 0)

    def __str__(self):
        if self.userID != None:
            return self.username


class Listing(models.Model):
    listID = models.UUIDField(default = uuid.uuid4, editable = False, primary_key = True)
    title = models.CharField(max_length=200)
    poster = models.ForeignKey(User, on_delete=models.CASCADE)
    description = models.TextField()
    status = models.PositiveSmallIntegerField()
    date = models.DateField()
    
    def __str__(self):
        if self.listID != None:
            return self.title
