package com.lop.api;

import com.lop.model.Link;
import com.lop.model.User;
import com.lop.model.World;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilderException;

/**
 * UsersResource REST Web Service
 *
 * @author Anh
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class UsersResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    public UsersResource(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    /**
     * Retrieves representation of an instance of com.lop.api.UsersResource
     * @return List of Users
     */
    @GET
    public List<User> getXml(@PathParam("boardId") String boardId) {
        List<User> users;
        if (boardId != null) {
            users = new ArrayList<>(World.getInstance().getBoards().getById().get(boardId).getUsers());
        } else {
            users = new ArrayList<>(World.getInstance().getUsers().getById().values());
        }
        for (User u : users) {
            Link.addLinks(u, uriInfo);
        }
        return users;
    }

    /**
     * POST method for creating an instance of UserResource add a new user to
     * the board when path is /boards/{boardId}/users but add new user when path
     * is /users
     *
     * @param boardId
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    public Response postXml(@PathParam("boardId") String boardId, User content) {
        if (boardId != null) {
            content = World.getInstance().getUsers().getById().get(Integer.toString(content.getId()));
            World.getInstance().getBoards().getById().get(boardId).addUser(content);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(content.getId())).build();
            return Response.created(uri)
                    .entity(content)
                    .build();
        } else {
            World.getInstance().getUsers().add(content);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(content.getId())).build();
            return Response.created(uri)
                    .entity(content)
                    .build();
        }
    }

    /**
     * Sub-resource locator method for {id}
     * @param id
     * @return an instance of UserResource
     */
    @Path("{id}")
    public UserResource getUserResource(@PathParam("id") String id) {
        return UserResource.getInstance(id, uriInfo);
    }
    
    /**
     * Login method.
     * 
     * @param request
     * @param u User object, with a Username and password
     * @return an HTTP response
     */
    @POST
    @Path("login")
    public Response login(@Context HttpServletRequest request, User u) {
        User me = World.getInstance().getUsers().login(u);
        System.out.println(me);
        if (me != null) {
            HttpSession session = request.getSession();
            me = Link.addLinks(me, uriInfo);
            session.setAttribute("me", me);
            System.out.println((User) session.getAttribute("me"));
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(UsersResource.class)
                    .path(Integer.toString(me.getId()))
                    .build();
            return Response.ok(uri)
                    .entity(me)
                    .build();
        } else {
            return Response.status(400)
                    .build();
        }
    }
    
    /**
     * logout method
     * @param request
     * @return an HTTP response
     */
    @GET
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok("<response>Logout successful</response>").build();
    }
    
    /**
     * sessionCheck method, checks if the user is logged in or not
     * @param request
     * @return an HTTP response
     */
    @GET
    @Path("me")
    public Response sessionCheck(@Context HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            User me = (User) session.getAttribute("me");
            me = Link.addLinks(me, uriInfo);
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(UsersResource.class)
                    .path(Integer.toString(me.getId()))
                    .build();
            return Response.ok(uri)
                    .entity(me)
                    .build();
        } catch (IllegalArgumentException | UriBuilderException | NullPointerException e) {
            return Response.status(400).entity("<response>Not logged in</response>").build();
        }
    }

    @Path("/{UserId}/tasks")
    public TasksResource getTasksResource() {
        return new TasksResource(uriInfo);
    }
}
